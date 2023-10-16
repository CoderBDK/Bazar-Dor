package com.coderbdk.bazardor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coderbdk.bazardor.di.remote.api.ApiResponse
import com.coderbdk.bazardor.di.remote.main.Product
import com.coderbdk.bazardor.di.remote.main.ProductCategory
import com.coderbdk.bazardor.di.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val repository: MainRepository = MainRepository()
    private val _productCategoryList = MutableLiveData<List<ProductCategory>>().apply {
        value = listOf()
    }
    private val _productList = MutableLiveData<List<Product>>().apply {
        value = listOf()
    }
    val productCategoryList: LiveData<List<ProductCategory>> = _productCategoryList
    val productList: LiveData<List<Product>> = _productList

    enum class ResponseState {
        INITIAL, ACCEPTED, FAILED
    }

    private val _responseState = MutableStateFlow(
        Pair("init", ResponseState.INITIAL)
    )
    val responseState: StateFlow<Pair<String, ResponseState>> = _responseState

    init {
        load()
    }

    private fun makeResponseState(
        message: String,
        response: ResponseState
    ): Pair<String, ResponseState> {
        return Pair(message, response)
    }

    private fun load() {
        if (productCategoryList.value?.size == 0) {
            _responseState.value = makeResponseState("init", ResponseState.INITIAL)
            repository.getProductCategoryList(
                ApiResponse(
                    {
                    }, {
                        _productCategoryList.postValue(it)
                        getProductByCategoryUID(it[0].uid)
                        _responseState.value = makeResponseState("accepted", ResponseState.ACCEPTED)
                    }, {
                        Log.e(javaClass.simpleName, it)
                        _responseState.value = makeResponseState(it, ResponseState.FAILED)
                    }
                )
            )
        }
    }

    fun getProductByCategoryUID(uid: Long) {
        if (_productList.value?.size == 0) {
            _responseState.value = makeResponseState("init", ResponseState.INITIAL)
            repository.getProductList(uid,
                ApiResponse(
                    {
                    }, {
                        _responseState.value = makeResponseState("accepted", ResponseState.ACCEPTED)
                        _productList.postValue(it)
                    }, {
                        Log.e(javaClass.simpleName, it)
                        _responseState.value = makeResponseState(it, ResponseState.FAILED)
                    }
                )
            )
        }
    }

    fun retry() {
        load()
    }


}