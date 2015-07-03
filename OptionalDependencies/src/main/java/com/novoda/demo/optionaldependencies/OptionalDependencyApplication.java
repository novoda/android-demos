package com.novoda.demo.optionaldependencies;

import android.app.Application;

import com.novoda.demo.optionaldependencies.OptionalDependencies;

public class OptionalDependencyApplication extends Application {
    private OptionalDependencies optionalDependencies;

    @Override
    public void onCreate() {
        super.onCreate();
        optionalDependencies = new OptionalDependencies();
    }

    public OptionalDependencies getOptionalDependencies() {
        return optionalDependencies;
    }
}
