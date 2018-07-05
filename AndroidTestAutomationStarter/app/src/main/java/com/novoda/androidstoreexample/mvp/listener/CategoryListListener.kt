package com.novoda.androidstoreexample.mvp.listener

import com.novoda.androidstoreexample.services.CategoryResponse

interface CategoryListListener : BaseListener {

    fun onSuccess(categoryResponse: CategoryResponse)
}