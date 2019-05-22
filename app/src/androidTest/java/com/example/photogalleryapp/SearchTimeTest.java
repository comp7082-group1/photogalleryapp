package com.example.photogalleryapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.TimingLogger;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.photogalleryapp.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SearchTimeTest {
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
    public void searchButtonTimeTest() {
        TimingLogger timings = new TimingLogger("Test", "search");
        timings.addSplit("Begin Test");
        onView(withId(R.id.main_searchButton)).perform(click());
        timings.addSplit("Main Activity Search Button Pressed");
        onView(withId(R.id.search_fromDate)).perform(typeText("19/01/15"), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText("19/01/15"), closeSoftKeyboard());
        onView(withId(R.id.search_keywordText)).perform(typeText("Group1"), closeSoftKeyboard());
        timings.addSplit("Search Form Populated");
        onView(withId(R.id.search_searchButton)).perform(click());
        timings.addSplit("Search Activity Search Button Pressed");
        timings.dumpToLog();
    }
}