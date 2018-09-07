package com.novoda.androidstoreexample.tests;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import com.novoda.androidstoreexample.R;
import com.novoda.androidstoreexample.ViewMatchers;
import com.novoda.androidstoreexample.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class EspressoJavaTestExampleWithoutFramework {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void firstNavigationTestWithoutFramework() {
        Matcher<RecyclerView.ViewHolder> categoryMatcher = ViewMatchers.withCategoryTitle("HATS");

        onView(android.support.test.espresso.matcher.ViewMatchers.withId(R.id.categoryListView)).perform(scrollToHolder(categoryMatcher), actionOnHolderItem(categoryMatcher, click()));

        Matcher<RecyclerView.ViewHolder> productMatcher = ViewMatchers.withProductTitle("hat white");

        onView(withId(R.id.productListView)).perform(scrollToHolder(productMatcher), actionOnHolderItem(productMatcher, click()));

        onView(withId(R.id.productDetailDescription)).check(matches(isDisplayed()));

    }

}
