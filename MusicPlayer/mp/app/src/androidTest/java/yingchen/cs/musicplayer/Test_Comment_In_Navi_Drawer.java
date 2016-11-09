package yingchen.cs.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.R.attr.x;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.view.KeyEvent.KEYCODE_PICTSYMBOLS;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.StringStartsWith.startsWith;


/**
 * test class includes tests that are testing a series functionality relative to comment.
 *
 * @author Ying Chen
 */

@RunWith(AndroidJUnit4.class)
public class Test_Comment_In_Navi_Drawer {//extends ApplicationTestCase<Application> {
                                          // extends ActivityInstrumentationTestCase2<MainActivity> {
    @Rule
    public final ActivityTestRule<MainActivity> mMainActivity =
                                                  new ActivityTestRule<>(MainActivity.class);

    private final String emoji_winking_face = "\uD83D\uDE09";
    private final String music_note = "\uD83C\uDFB5";

   // @Test
    public void commentsInNaviDrawerTest() {
        /*Bundle bundle = new Bundle();
        bundle.putString(MainActivity.POSITION_IN_NAVI_DRAWER, 4 + "");
        Intent intent = new Intent(mMainActivity.getActivity(), MainActivity.class);
        intent.putExtras(bundle);
        mMainActivity.launchActivity(intent);*/
        onView(withId(R.id.song_list_container)).check(matches(isDisplayed()));
    }

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
    public void submit() {
        onView(withId(R.id.song_list_container)).check(matches(isDisplayed()));
        pauseTestFor(5000);
        onView(withId(R.id.drawerLayout))
                .perform(swipeRight()).check(matches(isDisplayed()));
        pauseTestFor(5000);
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).check(matches(withText("")));
        pauseTestFor(1000);
        testPressKeys1();
        onView(withId(R.id.addButton))
                .check(matches(withText(R.string.submit))).perform(click());
        pauseTestFor(3000);
        testPressKeys2();
        onView(withId(R.id.addButton))
                .check(matches(withText(R.string.submit))).perform(click());
        pauseTestFor(5000);
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
        pauseTestFor(5000);
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
    }
    private void testPressKeys1(){
        onView(withId(R.id.todoText)).perform(clearText(), pressKey(KeyEvent.KEYCODE_T));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_H));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_I));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_S));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_I));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_S));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_A));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_F));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_U));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_N));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_A));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_P));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_P));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_PERIOD));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(replaceText("this is a fun app." + music_note),
                pressKey(KeyEvent.KEYCODE_MOVE_END));
        pauseTestFor(5000);
    }
    private void testPressKeys2(){
        onView(withId(R.id.todoText)).perform(clearText(), pressKey(KeyEvent.KEYCODE_I));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_C));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_A));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_N));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_D));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_O));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_T));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_H));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_I));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_S));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_A));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_L));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_L));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_SPACE));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_D));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_A));
        pauseTestFor(100);
        onView(withId(R.id.todoText)).perform(typeText(""), pressKey(KeyEvent.KEYCODE_Y));
        pauseTestFor(1000);
        onView(withId(R.id.todoText)).perform(replaceText("I can do this all day"+ emoji_winking_face),
                pressKey(KeyEvent.KEYCODE_MOVE_END));
        pauseTestFor(5000);
    }


}
