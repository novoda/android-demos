package com.novoda.demo.coroutines.simple

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_start_wars_quote.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StartWarsQuoteActivity : AppCompatActivity(R.layout.activity_start_wars_quote) {

    private val viewModel by viewModels<StarWarsQuoteViewModel>()

    private val eventChannel by lazy {
        Channel<UserEvent>().apply {
            refresh.setOnClickListener {
                offer(UserEvent.Refresh)
            }
        }
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            eventChannel.consumeEach { viewModel.onEvent(it) }
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                is QuoteState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    quote.text = ""
                }
                is QuoteState.Idle -> {
                    progressBar.visibility = View.GONE
                    quote.text = state.quote
                }
            }
        }
    }
}

sealed class UserEvent {

    object Refresh : UserEvent()

}

sealed class QuoteState {

    object Loading : QuoteState()
    data class Idle(val quote: String) : QuoteState()

}

class StarWarsQuoteViewModel : ViewModel() {

    private val stateChannel = Channel<QuoteState>()
    private val startService = StarWarsService()

    val state: LiveData<QuoteState> = stateChannel.consumeAsFlow().asLiveData()

    fun onEvent(userEvent: UserEvent) {
        viewModelScope.launch {
            when (userEvent) {
                UserEvent.Refresh -> refresh()
            }.collect {
                stateChannel.offer(it)
            }
        }
    }

    private fun refresh(): Flow<QuoteState> =
        flow {
            emit(QuoteState.Loading)
            val quote = startService.fetchSWFlow()
                .map { quote -> QuoteState.Idle(quote) }
            emitAll(quote)
        }

}

private fun StarWarsService.fetchSWFlow() = flow {
    delay(1_000)
    emit(fetchSW())
}
