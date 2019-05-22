package com.example.photogalleryapp;

import android.app.Activity;
import android.app.Instrumentation;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.photogalleryapp.mainactivity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CameraTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void ensureCameraOpenedOnSnapButtonClick() {
        onView(withId(R.id.main_SnapButton)).perform(click());
        intended(toPackage("com.android.camera2"));
    }

    @Test
    public void ensureTimestampDisplayedOnSnapButtonClick() {
        onView(withId(R.id.main_TimeStamp)).check(matches((withText("timeStamp"))));
        onView(withId(R.id.main_SnapButton)).perform(click());
        onView(withId(R.id.main_TimeStamp)).check(matches(not(withText("timeStamp"))));
    }

    @Test
    public void ensureCoordinatesDisplayedOnSnapButtonClick() {
        onView(withId(R.id.main_LocationText)).check(matches((withText("location"))));
        onView(withId(R.id.main_SnapButton)).perform(click());
        onView(withId(R.id.main_LocationText)).check(matches(not(withText("location"))));
    }
}