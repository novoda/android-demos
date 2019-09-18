package com.novoda.demo.coroutines.simple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

// TODO.
// 1. On activity start we see a loading on main screen
// 2. Loading while a 2 seconds background operation completes
// 3. After completing background operation print "DONE" on screen
// 4. Move loading / background job to separate components
// 5. Create a second service which will run in parallel and print "DONE IN PARALLEL" on screen
// 6. Cancelling scope

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.firstResult.observe(this, Observer {
            main_text_view.text = it
        })

        check_box.setOnClickListener {
            Log.e("COROUTINES", "CANCELLING")
            viewModel.viewModelScope.cancel()
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.load()
    }

}

class MainViewModel : ViewModel() {

    private val _firstResult = MutableLiveData<String>()
    val firstResult: LiveData<String> = _firstResult

    private val service = Service()

    fun load() {
        Log.e("COROUTINES", "START")
        viewModelScope.launch {
            _firstResult.value = "LOADING"
            val job = async {
                service.backgroundJobAsync()
            }
            val job2 = async {
                service.secondBackgroundJobAsync()
            }

            Log.e("COROUTINES", "${job.await()} ${job2.await()} ")
            val job3 = service.finalBackgroundJobAsync(job.await(), job2.await())
            _firstResult.value = job3.await()
        }


//    fun cancel() = uiScope?.cancel()

    }

    private class Service(val context: CoroutineContext = Dispatchers.IO) {
        suspend fun backgroundJobAsync() = withContext(context) {
            delay(4000)
            Log.e("COROUTINES", "JOB1")
            "HI"
        }

        suspend fun secondBackgroundJobAsync() = withContext(context) {
            delay(2000)
            Log.e("COROUTINES", "JOB2")
            "FRANK"
        }

        suspend fun finalBackgroundJobAsync(first: String, second: String) =
            withContext(Dispatchers.IO) {
                async {
                    Log.e("COROUTINES", "JOB3 $first $second")
                    "$first $second"
                }
            }
    }
}
