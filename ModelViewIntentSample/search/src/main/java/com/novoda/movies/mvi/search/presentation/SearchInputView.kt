package com.novoda.movies.mvi.search.presentation

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.presentation.SearchActivity.Action
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.search_bar.view.*

internal class SearchInputView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var searchInput: EditText
    private lateinit var clearTextButton: View

    private val actionStream: PublishSubject<Action> = PublishSubject.create()

    var currentQuery: String
        get() = searchInput.text.toString()
        set(text) {
            searchInput.fillWith(text)
            searchInput.setSelection(text.length)
        }

    val actions: Observable<Action>
        get() = actionStream

    private fun showKeyboard() {
        searchInput.showKeyboard()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.search_bar, this, true)

        searchInput = search_input_text
        clearTextButton = clear_button

        setupSearchInput()
        clearTextButton.setOnClickListener { clearText() }
    }

    private fun setupSearchInput() {
        searchInput.isSaveEnabled = false
        searchInput.setOnEditorActionListener { inputView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || enterKeyPressed(keyEvent)) {
                actionStream.onNext(Action.ExecuteSearch)
                inputView.hideKeyboard()
                inputView.clearFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchInput.addTextChangedListener(textChangedListener)
    }

    private fun clearText() {
        actionStream.onNext(Action.ClearQuery)
    }

    private fun enterKeyPressed(keyEvent: KeyEvent?): Boolean {
        return keyEvent != null &&
                keyEvent.keyCode == KEYCODE_ENTER &&
                keyEvent.action == ACTION_DOWN
    }

    private val textChangedListener = object :
            AfterTextChangedWatcher {
        override fun afterTextChanged(text: Editable) {
            actionStream.onNext(Action.ChangeQuery(text.toString()))

            val showClear = text.isNotEmpty()
            clearTextButton.visibility = if (showClear) View.VISIBLE else View.GONE
        }
    }

    private fun TextView.fillWith(text: String) {
        removeTextChangedListener(textChangedListener)
        setText(text)
        addTextChangedListener(textChangedListener)
    }

    private interface AfterTextChangedWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}

fun TextView.hideKeyboard() {
    clearFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

fun TextView.showKeyboard() {
    requestFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}
