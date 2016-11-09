import android.content.Intent;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.support.v4.ShadowLocalBroadcastManager;
import org.robolectric.shadows.support.v4.Shadows;
import org.robolectric.util.ActivityController;

import yingchen.cs.musicplayer.FrontendConstant;
import yingchen.cs.musicplayer.MainActivity;
import yingchen.cs.musicplayer.NavigationDrawerFragment;
import yingchen.cs.musicplayer.R;
import yingchen.cs.musicplayer.UserSignIn_OUT;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by yingchen on 10/30/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "yingchen.cs.musicplayer")
public class Test_Comment {


    private ActivityController<MainActivity> activityController;
    private MainActivity activity;

    @Before
    public void initMocks() {
        activityController = Robolectric.buildActivity(MainActivity.class);
        activity =activityController.create().start().visible().get();

    }
    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
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

        // 4 is the index of item in Navi Drawer.
        // 4 means Comment is clicked.
        mNaviDrawerFragment.itemClicked(view, 4);

        //actual intent that is started by startActivity.
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();

        // simulate the intent which is expected to be started.
        Bundle bundle = new Bundle();
        bundle.putString(FrontendConstant.POSITION_IN_NAVI_DRAWER, 4 + "");
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
        bundle.putString(FrontendConstant.POSITION_IN_NAVI_DRAWER, FrontendConstant.IS_SUGGESTION + "");
        bundle.putString(FrontendConstant.IDTOKEN, "token");
        Intent expectedIntent = new Intent();
        expectedIntent.putExtra("b",bundle);

        MainActivity mainA = (MainActivity) controller.newIntent(expectedIntent).resume().visible().get();

        assertNotNull(mainA.getIntent());

    }


    @Test
    public void test_firebaseInit_1_listview_MainActivity() {
        LinearLayout layout = mock(LinearLayout.class);
        ListView listView = mock(ListView.class);
        listView.setId(R.id.listView);
        layout.addView(listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);


        activityController = Robolectric.buildActivity(MainActivity.class);
        activity =activityController.create().start().visible().get();
        activity.firebaseInit(layout);

        assertEquals(listView.hasOnClickListeners(),false);
  }

    @Test
    public void test_firebaseInit_2_buttondelete_MainActivity() {

        LinearLayout layout = mock(LinearLayout.class);
        EditText text = mock(EditText.class);//(EditText) suggestionLayout.findViewById(R.id.todoText);
        Button button = mock(Button.class);//(Button) suggestionLayout.findViewById(R.id.addButton);
        layout.addView(text);
        layout.addView(button);
        assertEquals(button.hasOnClickListeners(),false);
    }

    @Test
    public void test_initSong_MainActivity() {
        Bundle b = mock(Bundle.class);
        activity.setSongClicked(false);
        activity.initSongs(b);
         View layout = activity.findViewById(R.id.music_list_view);
        assertNotNull(layout);
        assertEquals(View.VISIBLE, layout.getVisibility());
    }

    @Test
    public void test_initSong_songCliked_MainActivity() {
        Bundle b = mock(Bundle.class);

        activity.setSongClicked(true);

        activity.initSongs(b);
        View layout = activity.findViewById(R.id.music_list_view);
        assertNotNull(layout);
        assertEquals(View.VISIBLE, layout.getVisibility());
    }

    @Test
    public void test_initSong_default_no_bundle_MainActivity() {
        activity.setSongClicked(true);

        activity.initSongs(null);
        View layout = activity.findViewById(R.id.music_list_view);
        assertNotNull(layout);
        assertEquals(View.VISIBLE, layout.getVisibility());
    }

    @Test
    public void testSuggestion(){

        ActivityController controller = Robolectric.buildActivity(MainActivity.class).create().start();
        MainActivity mActi = (MainActivity) controller.get();
        assertNotNull(mActi);
        LinearLayout parent = (LinearLayout) mActi.findViewById (R.id.song_list_container);
        assertNotNull(shadowOf(mActi).getContentView());
        assertNotNull(parent);
        View v = shadowOf(mActi).getContentView().findViewById(R.id.song_list_container);
        assertNotNull(v);
        assertEquals(View.VISIBLE, v.getVisibility());

    }


}
