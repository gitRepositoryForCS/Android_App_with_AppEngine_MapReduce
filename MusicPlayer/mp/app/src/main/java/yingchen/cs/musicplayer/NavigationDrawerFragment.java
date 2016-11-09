package yingchen.cs.musicplayer;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import yingchen.cs.musicplayer.visualizer.Concepts.NamesInNavDrawer;


/**
 * Navigation Drawer fragment class.
 *
 * @author Ying Chen
 */
public class NavigationDrawerFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RecyclerViewAdapter.ClickListener {

    private static final int[] mIcons = {R.drawable.artists, R.drawable.albums, R.drawable.songs,
            R.drawable.discover, R.drawable.suggestion};
    private static final String[] mTitles = {"Artists", "Albums", "Songs", "Discover", "Comments"};
    private static final String TAG = "navigation_drawer";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private boolean mFinishFlag = false;
    private static boolean mSignedInState = false;

    private ISignIn_Out muserSignIn_Out;

    public NavigationDrawerFragment() {
    }  //Required empty public constructor

    public boolean getSignInState(){
        return mSignedInState;
    }

    //called in mainactivity.
    public ISignIn_Out getUserSignIn_Out(){
        return muserSignIn_Out;
    }

    public void setDrawerLayout(DrawerLayout d){
        mDrawerLayout = d;
    }
    public static List<NamesInNavDrawer> getData() {
        List<NamesInNavDrawer> data = new ArrayList<>();
        for (int i = 0; i < mTitles.length && i < mIcons.length; i++) {
            NamesInNavDrawer ds = new NamesInNavDrawer();
            ds.itemId = mIcons[i];
            ds.title = mTitles[i];
            data.add(ds);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    public void setUserSignIn_Out(UserSignIn_OUT u){
        muserSignIn_Out = u;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), getData());
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        muserSignIn_Out = new UserSignIn_OUT(this,layout);
       // muserSignIn_Out.initSignIn(layout);
        muserSignIn_Out.setResolvingError( savedInstanceState != null
                && savedInstanceState.getBoolean(FrontendConstant.STATE_RESOLVING_ERROR, false));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        setDrawerLayout(drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                }
                getActivity().invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                ;
            }
        });
    }

    // handles the event when items in the drawer are clicked.
    @Override
    public void itemClicked(View view, int position) {
        //Toast.makeText(getContext(), "itemclicked false", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString(FrontendConstant.POSITION_IN_NAVI_DRAWER, position + "");
        bundle.putString(FrontendConstant.IDTOKEN, muserSignIn_Out.getIdToken());
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        mDrawerLayout.closeDrawer(containerView);
    }

    public void onResume() {
        mFinishFlag = false;
        super.onResume();
        Log.i(TAG, "onResume!!!!!!! ");

    }

    public void onPause() {
        mFinishFlag = true;
        super.onPause();
        Log.i(TAG, "onPause!!!!!!! ");
    }


    // ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(getActivity(), "connected", Toast.LENGTH_LONG).show();
    }

    // ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "connection suspended", Toast.LENGTH_SHORT).show();
    }

    //   OnConnectionFailedListener callback.
    @Override
    public void onConnectionFailed( ConnectionResult result) {
        if (muserSignIn_Out.getResolvingError()) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                muserSignIn_Out.setResolvingError(true);
                result.startResolutionForResult(getActivity(), FrontendConstant.REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                muserSignIn_Out.getGoogleApiClient().connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            muserSignIn_Out.showErrorDialog(result.getErrorCode());
            muserSignIn_Out.setResolvingError(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop!!!!!!! ");
        muserSignIn_Out.cleanUp();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Navi_drawer", "onSaveInstanceState!!!!!!! ");
        outState.putBoolean(FrontendConstant.STATE_RESOLVING_ERROR, muserSignIn_Out.getResolvingError());
        if(mSignedInState) {
            outState.putString(FrontendConstant.USER_NAME, muserSignIn_Out.getUserName());
            outState.putString(FrontendConstant.USER_EMAIL,muserSignIn_Out.getmUserEmail());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == FrontendConstant.RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            muserSignIn_Out.handleSignInResult(result);
        } else if (requestCode == FrontendConstant.REQUEST_RESOLVE_ERROR) {
            muserSignIn_Out.setResolvingError(false);
            if (resultCode == FrontendConstant.RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!muserSignIn_Out.getGoogleApiClient().isConnecting() &&
                        !muserSignIn_Out.getGoogleApiClient().isConnected()) {
                    muserSignIn_Out.getGoogleApiClient().connect();
                }
            }
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

}
