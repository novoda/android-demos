package com.novoda.demo.coroutines.simple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

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
        viewModel.secondResult.observe(this, Observer {
            second_text_view.text = it
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
    private val _secondResult = MutableLiveData<String>()
    val secondResult: LiveData<String> = _secondResult

    private val service = Service()
    private val secondService = SecondService()

    fun load() {
        viewModelScope.launch {
            _firstResult.value = "LOADING"
            val job = service.backgroundJobAsync()
            _firstResult.value = job.await()
        }
        viewModelScope.launch {
            _secondResult.value = "ALSO LOADING"
            val job = secondService.backgroundJobAsync()
            _secondResult.value = job.await()
        }
    }

//    fun cancel() = uiScope?.cancel()

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
    suspend fun backgroundJobAsync() = withContext(Dispatchers.IO) {
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
