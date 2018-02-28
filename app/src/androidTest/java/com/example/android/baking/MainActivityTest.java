package com.example.android.baking;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String HEAD_NAME = "Ingredients";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void scrollGridViewItem() {
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.scrollToPosition(MainActivity.postResponses.size()+1));
    }
    @Test
    public void clickGridViewItem(){
        for(int i=0;i<MainActivity.postResponses.size();i++) {
            onView(withId(R.id.rv_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            onView(withId(R.id.tv_recipeName)).check(matches(withText(HEAD_NAME)));
            onView(isRoot()).perform(ViewActions.pressBack());
        }
    }

}
