package com.novoda.demo.coroutines.simple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

// TODO.
// 1. On activity start we see a loading on main screen
// 2. Loading while a 2 seconds background operation completes
// 3. After completing background operation print "DONE" on screen
// 4. Move loading / background job to separate components
// 5. Create a second service which will run in parallel and print "DONE IN PARALLEL" on screen

class MainActivity : AppCompatActivity() {

    private var viewModel: ViewModel = ViewModel(
        main_text_view.text = it,
        second_text_view.text = it
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check_box.setOnClickListener {
            Log.e("COROUTINES", "CANCELLING")
            viewModel.cancel()
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.load()
    }

}

private class ViewModel(private val callback: (String) -> Unit,
                        private val secondCallback: (String) -> Unit) {
    private var job: Job? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val service = Service()

    fun load() {
        job = uiScope.launch {
            callback("LOADING")
            val job = service.backgroundJobAsync()
            callback(job.await())
        }
    }

    fun cancel() = job?.cancel()

}

private class Service {
    suspend fun backgroundJobAsync() = withContext(Dispatchers.IO) {
        async {
            Thread.sleep(5000)
            if (this.isActive) {
                Log.e("COROUTINES", "JOB")
                "DONE"
            } else {
                ""
            }
        }
    }
}

private class SecondService {
    suspend fun backgroundJobAsync() = withContext(Dispathers.IO) {
        async {
            Thread.sleep(10000)
            if (this.isActive) {
                Log.e("COROUTINES", "SECOND JOB")
                "DONE IN PARALLEL"
            } else {
                ""
            }
        }
    }
}
