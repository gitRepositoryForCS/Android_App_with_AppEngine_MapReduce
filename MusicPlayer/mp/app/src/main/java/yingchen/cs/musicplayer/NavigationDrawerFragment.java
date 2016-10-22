package yingchen.cs.musicplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PluginStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

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

    public static final int IS_SUGGESTION = 4;
    public static final int IS_DISCOVER = 3;
    public static final String IDTOKEN = "idToken";
    public static boolean mSignedInState = false;
    /**
     * Standard activity result: operation succeeded.
     */
    public static final int RESULT_OK = -1;

    private static final int[] mIcons = {R.drawable.artists, R.drawable.albums, R.drawable.songs,
            R.drawable.discover, R.drawable.suggestion};
    private static final String[] mTitles = {"Artists", "Albums", "Songs", "Discover", "Comments"};

    private static final String TAG = "navigation_drawer";
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final int RC_SIGN_IN = 1;
    private static final String SHOW_FAIL_MASSAGE = "Sign in failed, please try again.";
    // Bool to track whether the app is already resolving an error
    private static boolean mResolvingError = false;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private boolean mFinishFlag = false;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;
    private TextView mSignInName;
    private TextView mSignOut;
    private RelativeLayout msignInLayout;
    private RelativeLayout mSigninResultLayout;
    private GoogleSignInAccount mSignInAcc;
    private String mIdToken;
    private static String mUserName;
    private static String mUserEmail;

    public NavigationDrawerFragment() {
    }  //Required empty public constructor

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
        Log.i(TAG, "onCreate!!!!!!! ");
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), getData());
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSignInButton = (SignInButton) layout.findViewById(R.id.sign_in_button);
        msignInLayout = (RelativeLayout) layout.findViewById(R.id.signInLayout);
        mSigninResultLayout = (RelativeLayout) layout.findViewById(R.id.sign_in_result_layout);

        initSignIn();
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
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
        bundle.putString(MainActivity.POSITION_IN_NAVI_DRAWER, position + "");
        bundle.putString(IDTOKEN, mIdToken);
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

    // init sign in config
    private void initSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestProfile()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setScopes(gso.getScopeArray());
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mGoogleApiClient.connect();
                signWindow();
            }
        });
    }

    //show the sign in window to user.
    private void signWindow() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(getActivity(), "connected", Toast.LENGTH_LONG).show();
    }

    // ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {

    }

    //   OnConnectionFailedListener callback.
    @Override
    public void onConnectionFailed( ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop!!!!!!! ");
        cleanUp();
    }

    /* sign out current acct, and revoke access given to this app. */
    private void cleanUp() {
        if (mGoogleApiClient.isConnected()) {
            signOut();
            revokeAccess();
            mGoogleApiClient.disconnect();
        }
    }



    /*sign out googleApiClient*/
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    /* Revokes access given to the current application. Future sign-in attempts
     * will require the user to re-consent to all requested scopes.*/
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    // The rest is about building error dialog.
    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Navi_drawer", "onSaveInstanceState!!!!!!! ");
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
        if(mSignedInState) {
            outState.putString(mUserName, mUserName);
            outState.putString(mUserEmail,mUserEmail);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //Toast.makeText(getActivity(), SHOW_FAIL_MASSAGE, Toast.LENGTH_LONG).show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    /* handle the updates depending on sign in result success or not. */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        mSignInAcc = result.getSignInAccount();
        if(mSignInAcc != null) mIdToken = mSignInAcc.getIdToken();
        boolean res = result.isSuccess();
        updateUI(res, mSignedInState);
    }

    public void updateUI(final boolean result, boolean signedInState ) {
        if (result || signedInState) {   // Signed in successfully, show authenticated UI.
            mSignedInState = true;
            if(result){
                mUserName = mSignInAcc.getDisplayName();
                mUserEmail = mSignInAcc.getEmail();
            }
            if(mSignInName == null)  {
                mSignInName = new TextView(getContext());
                mSignInName.setText( "Hey,  " + mUserName + "\r\n" + mUserEmail);
                mSignInName.setTextSize(20.0f);
                mSignInName.setId(R.id.textview_id_name);
                mSignInName.setVisibility(View.VISIBLE);
                mSigninResultLayout.addView(mSignInName);
            } else {
                mSignInName.setVisibility(View.VISIBLE);
            }

            if(mSignOut == null){
                mSignOut = new TextView(getContext());
                mSignOut.setText(R.string.sign_out);
                mSignOut.setTextSize(17.0f);
                mSignOut.setId(R.id.textview_id_sign_out);
                mSignOut.setTextColor(Color.parseColor("#0055dd"));
                mSignOut.setVisibility(View.VISIBLE);
                mSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(result) cleanUp();
                        mSignInName.setVisibility(View.INVISIBLE);
                        mSignOut.setVisibility(View.INVISIBLE);
                        mSigninResultLayout.removeView(mSigninResultLayout.findViewById(R.id.textview_id_sign_out));
                        mSigninResultLayout.removeView(mSigninResultLayout.findViewById(R.id.textview_id_name));
                        mSignInName = null;
                        mSignOut = null;
                        mSignInButton.setVisibility(View.VISIBLE);
                       // Toast.makeText(getActivity(), "You are signed out. ", Toast.LENGTH_SHORT).show();
                        mSignedInState = false;
                    }
                });
                RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                        DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);
                rParams.addRule(RelativeLayout.BELOW, mSignInName.getId());
                mSigninResultLayout.addView(mSignOut, rParams);
            }else {
                mSignOut.setVisibility(View.VISIBLE);
            }

            mSignInButton.setVisibility(View.INVISIBLE);

        } else {
            // Signed out, show unauthenticated UI.
            cleanUp();
            Toast.makeText(getActivity(), SHOW_FAIL_MASSAGE, Toast.LENGTH_LONG).show();
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            //((MainActivity) getActivity()).onDialogDismissed();
            mResolvingError = false;
        }
    }

}
