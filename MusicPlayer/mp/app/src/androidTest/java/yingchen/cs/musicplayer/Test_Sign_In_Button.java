package yingchen.cs.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * test class includes tests that are testing user sign-in UI.
 *
 * @author Ying Chen
 */

public class Test_Sign_In_Button {

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

    private void signIn(int i){

        onView(withId(R.id.song_list_container)).check(matches(isDisplayed()));
        pauseTestFor(3000);
        onView(withId(R.id.drawerLayout)).perform(swipeRight()).check(matches(isDisplayed()));
        pauseTestFor(3000);
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).perform(click());

        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
        UiObject allAppsButton = mDevice.findObject(new UiSelector().instance(i));
        try {
            allAppsButton.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            Assert.fail();
        }
        pauseTestFor(1000);

        onView(withId(R.id.song_list_container))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.drawerLayout)).perform(swipeRight()).check(matches(isDisplayed()));
        pauseTestFor(3000);

        onView(allOf(withId(R.id.textview_id_sign_out), withText("sign out"),
                withParent(allOf(withId(R.id.sign_in_result_layout),
                        withParent(withId(R.id.fragment_navigation_drawer)),withEffectiveVisibility(VISIBLE))),
                isDisplayed()))
                .perform(click());
        pauseTestFor(2000);
    }

    @Test
    public void signInButtonInNaviDrawerTest() {
        signIn(0);   // use index 0 to choose the first account in the chooser dialogue. 
        signIn(1);  // it seems that UiSelector().instance() is not quite reliable. 
                    // use index 1 won't make it choose the second account.
                    // but use index 2 will cause index out of range exception. 
    }
}
