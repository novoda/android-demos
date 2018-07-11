package com.novoda.androidstoreexample.tests;

import android.support.test.rule.ActivityTestRule;

import com.novoda.androidstoreexample.activities.MainActivity;
import com.novoda.androidstoreexample.pageobjects.HeaderPageObject;

import org.junit.Rule;
import org.junit.Test;

public class JavaHeaderTests {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void actionBarIsDisplayed() {
        HeaderPageObject headerPageObject = new HeaderPageObject();
        headerPageObject.verifyHeader();
    }

}
