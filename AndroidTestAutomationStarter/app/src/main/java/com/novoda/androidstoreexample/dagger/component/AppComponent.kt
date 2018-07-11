package com.novoda.androidstoreexample.dagger.component

import com.novoda.androidstoreexample.dagger.basket.BasketComponent
import com.novoda.androidstoreexample.dagger.basket.BasketModule
import com.novoda.androidstoreexample.dagger.categoryList.CategoryListComponent
import com.novoda.androidstoreexample.dagger.categoryList.CategoryListModule
import com.novoda.androidstoreexample.dagger.categoryList.ProductListComponent
import com.novoda.androidstoreexample.dagger.categoryList.ProductListModule
import com.novoda.androidstoreexample.dagger.module.BasketServiceModule
import com.novoda.androidstoreexample.dagger.module.ClientModule
import com.novoda.androidstoreexample.dagger.module.HostModule
import com.novoda.androidstoreexample.dagger.module.RetrofitModule
import com.novoda.androidstoreexample.dagger.productDetails.ProductDetailsComponent
import com.novoda.androidstoreexample.dagger.productDetails.ProductDetailsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(HostModule::class), (RetrofitModule::class), (ClientModule::class), (BasketServiceModule::class)])
interface AppComponent {
    fun injectCategory(categoryListModule: CategoryListModule): CategoryListComponent
    fun injectProducts(productListModule: ProductListModule): ProductListComponent
    fun injectProductDetails(productDetailsModule: ProductDetailsModule): ProductDetailsComponent
    fun injectBasket(basketModule: BasketModule): BasketComponent
}