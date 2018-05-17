package com.novoda.androidstoreexample.stepdefs

import cucumber.api.DataTable
import cucumber.api.java8.En
import java.lang.Thread.sleep

class StepDefs: En {

    init {
        Given("^The user has launched the app$") {
            println("banana")
            fun launch(){
                sleep(1000)

            }
            // Write code here that turns the phrase above into concrete actions
//            throw PendingException()
        }

        Then("^The user can see the following Product Categories:$") { arg1: DataTable ->
            // Write code here that turns the phrase above into concrete actions
            // For automatic transformation, change DataTable to one of
            // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
            // E,K,V must be a scalar (String, Integer, Date, enum etc)
            assert(true)
//            throw PendingException()
        }

    }
}
