package com.coderbdk.bazardor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coderbdk.bazardor.di.remote.api.ApiResponse
import com.coderbdk.bazardor.di.remote.main.ProductCategory
import com.coderbdk.bazardor.di.repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository: MainRepository = MainRepository()
    private val _productCategoryList = MutableLiveData<List<ProductCategory>>().apply {
        value = listOf()
    }
    val productCategoryList: LiveData<List<ProductCategory>> = _productCategoryList

    init {
        if(productCategoryList.value?.size ==0){
            repository.getProductCategoryList(
                ApiResponse(
                    {
                    },{
                        _productCategoryList.postValue(it)
                    },{
                        Log.e(javaClass.simpleName,it)
                    }
                )
            )
        }
    }
}