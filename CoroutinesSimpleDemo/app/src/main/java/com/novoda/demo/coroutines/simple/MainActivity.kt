package com.novoda.demo.coroutines.simple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

// TODO.
//  1. Text view with a timer
//  2. Timer increases value per second
//  3. Timer increase must be calculated in a background thread
//  4. Collaborator doing this
// -> Activity should not know anything about coroutines.

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        updateCounter { counter: String ->
            main_text_view.text = counter
        }
    }

    private fun updateCounter(block: (String) -> Unit) {
        var counter = 1
        block(counter.toString())

        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                counter++
                Log.d("MainActivity", "Loop $counter - Increased in thread: ${Thread.currentThread().name}")

                withContext(Dispatchers.Main) {
                    block(counter.toString())
                }
                Log.d("MainActivity", "Loop $counter finished")
            }
        }
    }
}
