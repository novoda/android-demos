package com.novoda.kotlincoroutinestraining

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

class MainPresenter: ViewModel() {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun observe(function: (String) -> Unit) {

        Log.v("APP", "before start in " + Thread.currentThread().name)

        scope.launch {
            Log.v("APP", "start in " + Thread.currentThread().name)

            function("loading")

            // display loading
            val firstTask = async(Dispatchers.IO) {
                blockOne()
            }

            val secondTask = async(Dispatchers.IO) {
                blockTwo()
            }

            // execute tasks in serial

            val firstValue = firstTask.await()
            val secondValue = secondTask.await()

            Log.v("APP", "ends in " + Thread.currentThread().name)

            function(firstValue + secondValue)
        }
    }

    private suspend fun blockOne(): String = suspendCoroutine {
        GlobalScope.launch(Dispatchers.IO) {

        }
        Log.v("APP", "firstTask in " + Thread.currentThread().name)
        Log.v("APP", "start wait 1s")
        Thread.sleep(1000)
        Log.v("APP", "end wait 1s")
        it.resumeWith(Result.success("first"))
    }

    private fun blockTwo(): String {
        Log.v("APP", "secondTask in " + Thread.currentThread().name)
        Log.v("APP", "start wait 3s")
        Thread.sleep(3000)
        Log.v("APP", "end wait 3s")
        return "second"
    }

    fun stop() {
        scope.cancel()
    }

}
