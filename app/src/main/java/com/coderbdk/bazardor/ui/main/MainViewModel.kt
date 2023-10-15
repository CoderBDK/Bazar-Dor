package com.coderbdk.bazardor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coderbdk.bazardor.di.remote.api.ApiResponse
import com.coderbdk.bazardor.di.remote.main.Product
import com.coderbdk.bazardor.di.remote.main.ProductCategory
import com.coderbdk.bazardor.di.repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository: MainRepository = MainRepository()
    private val _productCategoryList = MutableLiveData<List<ProductCategory>>().apply {
        value = listOf()
    }
    private val _productList = MutableLiveData<List<Product>>().apply {
        value = listOf()
    }
    val productCategoryList: LiveData<List<ProductCategory>> = _productCategoryList
    val productList: LiveData<List<Product>> = _productList

    init {
        if(productCategoryList.value?.size ==0){
            repository.getProductCategoryList(
                ApiResponse(
                    {
                    },{
                        _productCategoryList.postValue(it)
                        getProductByCategoryUID(it[0].uid)
                    },{
                        Log.e(javaClass.simpleName,it)
                    }
                )
            )
        }
    }

    fun getProductByCategoryUID(uid: Long){
        repository.getProductList(uid,
            ApiResponse(
                {
                },{
                    _productList.postValue(it)
                },{
                    Log.e(javaClass.simpleName,it)
                }
            )
        )
    }


}