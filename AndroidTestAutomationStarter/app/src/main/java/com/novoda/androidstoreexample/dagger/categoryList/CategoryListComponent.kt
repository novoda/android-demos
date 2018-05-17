package com.novoda.androidstoreexample.dagger.categoryList

import dagger.Subcomponent
import com.novoda.androidstoreexample.activities.MainActivity

@Subcomponent(modules = arrayOf(CategoryListModule::class))
interface CategoryListComponent {

    fun inject(mainActivity: MainActivity)
}