package yingchen.cs.musicplayer;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test_user_signIn_signOut {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_user_signIn_signOut() {


        ViewInteraction rc = onView(
                allOf(withText("Sign in"),
                        withParent(allOf(withId(R.id.sign_in_button),
                                withParent(withId(R.id.signInLayout)))),
                        isDisplayed()));
        rc.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textview_id_sign_out), withText("sign out"),
                        withParent(allOf(withId(R.id.sign_in_result_layout),
                                withParent(withId(R.id.fragment_navigation_drawer)))),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.songsName), withText("Comments"), isDisplayed()));
        textView2.perform(click());

        pressBack();

        ViewInteraction rc2 = onView(
                allOf(withText("Sign in"),
                        withParent(allOf(withId(R.id.sign_in_button),
                                withParent(withId(R.id.signInLayout)))),
                        isDisplayed()));
        rc2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textview_id_sign_out), withText("sign out"),
                        withParent(allOf(withId(R.id.sign_in_result_layout),
                                withParent(withId(R.id.fragment_navigation_drawer)))),
                        isDisplayed()));
        textView3.perform(click());

    }

}
