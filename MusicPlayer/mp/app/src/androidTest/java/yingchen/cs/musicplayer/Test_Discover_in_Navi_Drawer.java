package yingchen.cs.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;

/**
 * test class includes tests that are testing a series functionality relative to discover.
 *
 * @author Ying Chen
 */
@RunWith(AndroidJUnit4.class)
public class Test_Discover_in_Navi_Drawer {

    @Rule
    public final ActivityTestRule<MainActivity> mMainActivity =
                                                   new ActivityTestRule<>(MainActivity.class);

    public  static ViewAction swipeRight() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void discoverInNaviDrawerTest() {
        onView(withId(R.id.song_list_container)).check(matches(isDisplayed()));
        onView(withId(R.id.drawerLayout))
                .perform(swipeRight()).check(matches(isDisplayed()));
        pauseTestFor(5000);
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        pauseTestFor(1000);
        onView(withId(R.id.song_list_container))
                .check(ViewAssertions.matches(isDisplayed()));


        onData(anything())
                .inAdapterView(withId(R.id.music_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.song_title))
                .check(matches(withText(startsWith("n1"))));
    }



    }
