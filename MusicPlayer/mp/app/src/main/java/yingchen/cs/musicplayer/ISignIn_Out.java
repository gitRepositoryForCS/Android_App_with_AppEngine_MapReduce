package yingchen.cs.musicplayer;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by yingchen on 10/29/16.
 */

public interface ISignIn_Out {
    public void setGoogleApiClient(GoogleApiClient gac);
    public void setSignInLayout(View layout);
    public void setSignInResultLayout(View layout);
    public void setSignInButton(View layout);
    public void setResolvingError(boolean mre);
    public void setGoogleSignInOptions(GoogleSignInOptions mGso);
    public void setUserName(String un);
    public void setmUserEmail(String ue);
    public void setIdToken(String idToken);
    public void setSignInNameTextView(TextView t);
    public void setSignOutTextView(TextView t);
    public void setSignedInState(boolean b);

    public boolean getResolvingError();
    public RelativeLayout getSignInLayout();
    public  String getmUserEmail();
    public RelativeLayout getSignInResultLayout();
    public GoogleSignInOptions getGoogleSignInOptions();
    public GoogleApiClient getGoogleApiClient();
    public SignInButton getSignInButton();
    public String getUserName();
    public String getIdToken();
    public TextView getSignInNameTextView();
    public TextView getSignOutTextView();

    public void handleSignInResult(GoogleSignInResult result);

    public void cleanUp();

    public void showErrorDialog(int errorCode);
    public void updateUI(final boolean result, boolean signedInState );
}
