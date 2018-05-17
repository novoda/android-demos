package com.novoda.androidstoreexample

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
        features = ["/src/androidTest/assets/features"],
        tags = ["not @ignored"])

class RunCucumberTest

