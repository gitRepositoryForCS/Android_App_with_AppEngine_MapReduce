import android.content.Intent;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRequestDirector;
import org.apache.http.protocol.HttpContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.httpclient.FakeHttp;
import org.robolectric.shadows.httpclient.TestHttpResponse;
import org.robolectric.util.ActivityController;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import yingchen.cs.musicplayer.FetchData;
import yingchen.cs.musicplayer.FrontendConstant;
import yingchen.cs.musicplayer.MainActivity;
import yingchen.cs.musicplayer.NavigationDrawerFragment;
import yingchen.cs.musicplayer.UserSignIn_OUT;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by yingchen on 10/30/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "yingchen.cs.musicplayer")
public class Test_Discover {

    private ActivityController<MainActivity> activityController;
    private MainActivity activity;

    //private String TEST_FILE_PATH = "src/test/resources/mapReduce_output.txt";
    //private String MEMI_TYPE = "text/plain";

    private DefaultRequestDirector requestDirector;
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

    @Before
    public void initMocks() {
        activityController = Robolectric.buildActivity(MainActivity.class);
        activity =activityController.create().start().visible().get();
        connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                return 0;
            }
        };
        requestDirector = new DefaultRequestDirector(null, null, null, connectionKeepAliveStrategy, null, null, null, null, null, null, null, null);

    }
    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
    }

    @Test
    public void shouldReturnRequestsByRule_MatchingMethod() throws Exception {
        FakeHttp.setDefaultHttpResponse(404, "no such page");
        FakeHttp.addHttpResponseRule(HttpPost.METHOD_NAME, FrontendConstant.URL,
                new TestHttpResponse(200, "a cheery response body"));

       // HttpResponse response = requestDirector.execute(null, new HttpGet(FrontendConstant.URL), null);
        FetchData fd = new FetchData(activity, FrontendConstant.IS_DISCOVER);
        fd.execute(new String[]{FrontendConstant.URL});
        String s = fd.getResult();

        HttpResponse response = fd.getHttpResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 404);
    }

    @Test
    public void testItemClicked() {
        NavigationDrawerFragment mNaviDrawerFragment = new NavigationDrawerFragment();
        startFragment(mNaviDrawerFragment);
        assertNotNull(mNaviDrawerFragment.getView());

        UserSignIn_OUT u = mock(UserSignIn_OUT.class);
        mNaviDrawerFragment.setUserSignIn_Out(u);
        mNaviDrawerFragment.setDrawerLayout(mock(DrawerLayout.class));
        View view = mock(RecyclerView.class);

        // 3 is the index of item in Navi Drawer.
        // 3 means Discover is clicked.
        mNaviDrawerFragment.itemClicked(view, 3);

        //actual intent that is started by startActivity.
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();

        // simulate the intent which is expected to be started.
        Bundle bundle = new Bundle();
        bundle.putString(FrontendConstant.POSITION_IN_NAVI_DRAWER, 3 + "");
        bundle.putString(FrontendConstant.IDTOKEN, null);
        Intent expectedIntent = new Intent(activity, MainActivity.class);
        expectedIntent.putExtras(bundle);

        assertEquals(expectedIntent, actualIntent);
    }

    @Test
    public void testOnResume(){

        ActivityController controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity mActi = (MainActivity) controller.get();

        Bundle bundle = new Bundle();
        bundle.putString(FrontendConstant.POSITION_IN_NAVI_DRAWER, FrontendConstant.IS_DISCOVER + "");
        bundle.putString(FrontendConstant.IDTOKEN, "token");
        Intent expectedIntent = new Intent();
        expectedIntent.putExtra("b",bundle);

        MainActivity mainA = (MainActivity) controller.newIntent(expectedIntent).resume().visible().get();

        assertNotNull(mainA.getIntent());

    }

}