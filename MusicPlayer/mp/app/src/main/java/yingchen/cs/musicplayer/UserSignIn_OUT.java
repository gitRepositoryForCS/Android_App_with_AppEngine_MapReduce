package yingchen.cs.musicplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

/**
 * @author yingchen
 */
public class UserSignIn_OUT implements ISignIn_Out {

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final int RC_SIGN_IN = 1;
    private static final String SHOW_FAIL_MASSAGE = "Sign in failed, please try again.";
    public static boolean mSignedInState = false;
    private static String mUserName;
    private static String mUserEmail;
    // Bool to track whether the app is already resolving an error
    private static boolean mResolvingError = false;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGso;
    private SignInButton mSignInButton;
    private TextView mSignInName;
    private TextView mSignOut;
    private RelativeLayout msignInLayout;
    private RelativeLayout mSigninResultLayout;
    private GoogleSignInAccount mSignInAcc;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String mIdToken;

    public UserSignIn_OUT(NavigationDrawerFragment navi, View layout,
                          GoogleSignInOptions gso, GoogleApiClient gac) {
        mNavigationDrawerFragment = navi;
        // initSignIn(lo);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        setSignInLayout(layout);
        setSignInResultLayout(layout);
        setGoogleSignInOptions(gso);
        setGoogleApiClient(gac);
        setSignInButton(layout);
    }

    public UserSignIn_OUT(NavigationDrawerFragment navi, View layout) {
        mNavigationDrawerFragment = navi;
        // initSignIn(lo);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        setSignInLayout(layout);
        setSignInResultLayout(layout);

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestProfile()
                .requestIdToken(mNavigationDrawerFragment.getString(R.string.server_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(mNavigationDrawerFragment.getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .addConnectionCallbacks(mNavigationDrawerFragment)
                .addOnConnectionFailedListener(mNavigationDrawerFragment)
                .build();
        setSignInButton(layout);

    }

    @Override
    public boolean getResolvingError() {
        return mResolvingError;
    }

    @Override
    public void setResolvingError(boolean mre) {
        mResolvingError = mre;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void setGoogleApiClient(GoogleApiClient gac) {
        mGoogleApiClient = gac;
    }

    @Override
    public String getUserName() {
        return mUserName;
    }

    @Override
    public void setUserName(String un) {
        mUserName = un;
    }

    @Override
    public String getmUserEmail() {
        return mUserEmail;
    }

    @Override
    public void setmUserEmail(String ue) {
        mUserEmail = ue;
    }

    @Override
    public RelativeLayout getSignInResultLayout() {
        return mSigninResultLayout;
    }

    @Override
    public void setSignInResultLayout(View layout) {
        mSigninResultLayout = (RelativeLayout) layout.findViewById(R.id.sign_in_result_layout);
    }

    @Override
    public GoogleSignInOptions getGoogleSignInOptions() {
        return mGso;
    }

    @Override
    public void setGoogleSignInOptions(GoogleSignInOptions gso) {
        mGso = gso;
    }

    @Override
    public String getIdToken() {
        return mIdToken;
    }

    @Override
    public void setIdToken(String idToken) {
        mIdToken = idToken;
    }

    @Override
    public SignInButton getSignInButton() {
        return mSignInButton;
    }

    @Override
    public void setSignInButton(View layout) {
        mSignInButton = (SignInButton) layout.findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setScopes(mGso.getScopeArray());
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mGoogleApiClient.connect();
                signWindow();
            }
        });
    }

    @Override
    public RelativeLayout getSignInLayout() {
        return msignInLayout;
    }

    @Override
    public void setSignInLayout(View layout) {
        msignInLayout = (RelativeLayout) layout.findViewById(R.id.signInLayout);
    }

    @Override
    public TextView getSignInNameTextView(){
        return mSignInName;
    }

    @Override
    public void setSignInNameTextView(TextView t){
        mSignInName = t;
    }
    @Override
    public void setSignOutTextView(TextView t){
        mSignOut = t;
    }

    @Override
    public TextView getSignOutTextView(){
        return mSignOut;
    }
    @Override
    public void setSignedInState(boolean b){
        mSignedInState = b;
    }

    public void updateUI(final boolean result, boolean signedInState ){
        if (!result && !signedInState) {// Signed out, show unauthenticated UI.
            cleanUp();
            return;
        }
        // Signed in successfully, show authenticated UI.
        setSignedInState(true);
        if (result) {
            setUserName(mSignInAcc.getDisplayName());
            setmUserEmail(mUserEmail = mSignInAcc.getEmail());
        }
        if (mSignInName == null) {
            mSignInName = new TextView(mNavigationDrawerFragment.getContext());
            mSignInName.setText("Hey,  " + mUserName + "\r\n" + mUserEmail);
            mSignInName.setTextSize(20.0f);
            mSignInName.setId(R.id.textview_id_name);
            mSignInName.setVisibility(View.VISIBLE);
            setSignInNameTextView(mSignInName);
            mSigninResultLayout.addView(mSignInName);
        } else {
            mSignInName.setVisibility(View.VISIBLE);
        }
        if (mSignOut == null) {
            mSignOut = new TextView(mNavigationDrawerFragment.getContext());
            mSignOut.setText(R.string.sign_out);
            mSignOut.setTextSize(17.0f);
            mSignOut.setId(R.id.textview_id_sign_out);
            mSignOut.setTextColor(Color.parseColor("#0055dd"));
            mSignOut.setVisibility(View.VISIBLE);
            mSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (result) cleanUp();
                    mSignInName.setVisibility(View.INVISIBLE);
                    mSignOut.setVisibility(View.INVISIBLE);
                    mSigninResultLayout.removeView(mSigninResultLayout.findViewById(R.id.textview_id_sign_out));
                    mSigninResultLayout.removeView(mSigninResultLayout.findViewById(R.id.textview_id_name));
                    mSignInName = null;
                    mSignOut = null;
                    mSignInButton.setVisibility(View.VISIBLE);
                    // Toast.makeText(getActivity(), "You are signed out. ", Toast.LENGTH_SHORT).show();
                    setSignedInState(false);
                }
            });
            setSignOutTextView(mSignOut);
            RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                    DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);
            rParams.addRule(RelativeLayout.BELOW, mSignInName.getId());
            mSigninResultLayout.addView(mSignOut, rParams);
        } else {
            mSignOut.setVisibility(View.VISIBLE);
        }
        mSignInButton.setVisibility(View.INVISIBLE);

    }
    /* handle the updates depending on sign in result success or not. called in
    NaviDrawerFragement onActivityResult */
    @Override
    public void handleSignInResult(GoogleSignInResult res) {
        mSignInAcc = res.getSignInAccount();
        if (mSignInAcc != null) setIdToken(mSignInAcc.getIdToken());
        final boolean result = res.isSuccess();
        updateUI(result, mSignedInState);
    }

    //show the sign in window to user. called in setSigninButton
    private void signWindow() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mNavigationDrawerFragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /* sign out current acct, and revoke access given to this app. */
    public void cleanUp() {
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

    /* Creates a dialog for an error message, called in NavigationDrawerFragment.*/
    public void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(mNavigationDrawerFragment.getFragmentManager(), "errordialog");
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
