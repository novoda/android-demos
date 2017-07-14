package com.novoda.demo.tddpersistence;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TaskNamedMatcher extends TypeSafeMatcher<Task> {

    private String expectedName;

    public TaskNamedMatcher(String expectedName) {
        this.expectedName = expectedName;
    }

    @Override
    protected boolean matchesSafely(Task actualTask) {
        return actualTask.getName().equals(expectedName);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("a task named ")
                .appendValue(expectedName);
    }

    public static TaskNamedMatcher aTaskNamed(String expectedName) {
        return new TaskNamedMatcher(expectedName);
    }
}
