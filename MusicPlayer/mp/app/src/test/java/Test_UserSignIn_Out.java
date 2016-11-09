/**
 * Created by yingchen on 10/16/16.
 */

import android.content.Context;
import android.support.v4.BuildConfig;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ActivityController;

import yingchen.cs.musicplayer.ISignIn_Out;
import yingchen.cs.musicplayer.MainActivity;
import yingchen.cs.musicplayer.NavigationDrawerFragment;
import yingchen.cs.musicplayer.R;
import yingchen.cs.musicplayer.UserSignIn_OUT;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "yingchen.cs.musicplayer")
public class Test_UserSignIn_Out {
    @Mock
    Context mMockContext;
    @Mock
    NavigationDrawerFragment mNaviDrawerFragment;
    @Mock
    View layout;
    @Mock
    GoogleSignInOptions mGso;
    @Mock
    GoogleApiClient mGac;

    private ActivityController<MainActivity> activityController;
    private MainActivity activity;

    private ISignIn_Out mUserSignIn_Out;

    @Before
    public void initMocks() {
        activityController = Robolectric.buildActivity(MainActivity.class);
        activity =activityController.create().start().visible().get();
        layout = activity.findViewById(R.id.fragment_navigation_drawer);
        mGso = mock(GoogleSignInOptions.class);
        mGac = mock(GoogleApiClient.class);
        mNaviDrawerFragment = mock(NavigationDrawerFragment.class);
        mUserSignIn_Out = new UserSignIn_OUT(mNaviDrawerFragment,layout,mGso,mGac);
    }
    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
    }


    @Test
    public void testHandleSignInResult_null(){

        NavigationDrawerFragment mNaviDrawerFragment = new NavigationDrawerFragment();
        SupportFragmentTestUtil.startFragment(mNaviDrawerFragment);
        assertNotNull(mNaviDrawerFragment.getView());


        mUserSignIn_Out = new UserSignIn_OUT(mNaviDrawerFragment,layout,mGso,mGac);
        mUserSignIn_Out.setSignedInState(true);

        GoogleSignInResult result = mock(GoogleSignInResult.class);
        mUserSignIn_Out.handleSignInResult(result);
        assertNotNull(mUserSignIn_Out.getSignInNameTextView());
        assertNotNull(mUserSignIn_Out.getSignOutTextView());
    }


    @Test
    public void testHandleSignInResult_nonNull(){
        GoogleSignInResult result = mock(GoogleSignInResult.class);
        NavigationDrawerFragment mNaviDrawerFragment = new NavigationDrawerFragment();
        SupportFragmentTestUtil.startFragment(mNaviDrawerFragment);
        assertNotNull(mNaviDrawerFragment.getView());

        mUserSignIn_Out = new UserSignIn_OUT(mNaviDrawerFragment,layout,mGso,mGac);
        TextView t = mock(TextView.class);
        TextView t1 = mock(TextView.class);
        mUserSignIn_Out.setSignInNameTextView(t);
        mUserSignIn_Out.setSignOutTextView(t1);
        mUserSignIn_Out.handleSignInResult(result);
        assertNotNull(mUserSignIn_Out.getSignInNameTextView());
        assertNotNull(mUserSignIn_Out.getSignOutTextView());

        assertThat(mUserSignIn_Out.getSignInNameTextView().getVisibility(),is(View.VISIBLE));
        assertThat(mUserSignIn_Out.getSignOutTextView().getVisibility(),is(View.VISIBLE));
    }

    @Test
    public void testCleanUp(){
        mGac = mock(GoogleApiClient.class);
        mGac.connect();

        mUserSignIn_Out.setGoogleApiClient(mGac);
        mUserSignIn_Out.cleanUp();
        assertThat(mGac.isConnected(),is(false));

    }

    @Test
    public void testNavigationDrawerFragment(){
        NavigationDrawerFragment sampleFragment = new NavigationDrawerFragment();
        SupportFragmentTestUtil.startFragment(sampleFragment);
        assertNotNull(sampleFragment.getView());
    }


    @Test
    public void testResolvingError() {
        mUserSignIn_Out.setResolvingError(true);
        assertThat(mUserSignIn_Out.getResolvingError(),is(true));
    }
    @Test
    public void testSignInLayout() {
        mUserSignIn_Out.setSignInLayout(layout);
        assertNotNull(mUserSignIn_Out.getSignInLayout());
    }
    @Test
    public void testSignInResultLayout() {
        mUserSignIn_Out.setSignInResultLayout(layout);
        assertNotNull(mUserSignIn_Out.getSignInResultLayout());
    }
    @Test
    public void testGoogleSignInOptions() {
        mUserSignIn_Out.setGoogleSignInOptions(mGso);
        assertNotNull(mUserSignIn_Out.getGoogleSignInOptions());
    }
    @Test
    public void testGoogleApiClient() {
        mUserSignIn_Out.setGoogleApiClient(mGac);
        assertNotNull(mUserSignIn_Out.getGoogleApiClient());
    }
    @Test
    public void testSignInButton() {
        mUserSignIn_Out.setSignInButton(layout);
        assertNotNull(mUserSignIn_Out.getSignInButton());
    }
    @Test
    public void testUserEmail(){
        mUserSignIn_Out.setmUserEmail("email");
        assertThat(mUserSignIn_Out.getmUserEmail(),is("email"));
    }

}
