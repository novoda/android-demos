package com.novoda.androidstoreexample.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.novoda.androidstoreexample.dagger.App
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.mvp.view.BaseView

abstract class BaseActivity : AppCompatActivity(), BaseView {

    abstract fun getActivityLayout(): Int

    abstract fun injectDependencies(appComponent: AppComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getActivityLayout())
        injectDependencies(App.component)
    }

    override fun showMessage(message: String) {
    }
}
