package com.coderbdk.bazardor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.bazardor.domain.remote.api.ApiResponse
import com.coderbdk.bazardor.domain.remote.main.Product
import com.coderbdk.bazardor.domain.remote.main.ProductCategory
import com.coderbdk.bazardor.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(val repository: MainRepository): ViewModel() {

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
    enum class ResponseType{
        PRODUCT_CATEGORY,PRODUCT_ITEM
    }

    private val _responseState = MutableStateFlow(
        Pair("init", ResponseState.INITIAL)
    )
    val responseState: StateFlow<Pair<String, ResponseState>> = _responseState

    init {
        viewModelScope.launch {
            var mList = repository.getLocalProductCategoryList()
            if(mList.isNotEmpty()){
                _productCategoryList.postValue(mList)
                _responseState.value = makeResponseState("done",ResponseState.ACCEPTED)
            }else{
                load()
            }

        }
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
                        viewModelScope.launch {
                            it.forEach { data->
                               data.apply {
                                   repository.upsertProductCategory(ProductCategory(uid,name,imgURL))
                               }
                            }
                        }
                        _productCategoryList.postValue(it)
                        getProductByCategoryUID(it[0].uid, repository.formatTime(System.currentTimeMillis()))
                        _responseState.value = makeResponseState("accepted", ResponseState.ACCEPTED)
                    }, {
                        Log.e(javaClass.simpleName, it)
                        _responseState.value = makeResponseState(it, ResponseState.FAILED)
                    }
                )
            )
        }
    }

    private val oldUID = MutableLiveData<Long>().apply {
        value = -1
    }
    private val oldDate = MutableLiveData<String>().apply {
        value = "00-00-00"
    }
    fun getProductByCategoryUID(uid: Long, date: String) {

        viewModelScope.launch {
            if(repository.isLocalProductFound(uid,date)){
                _responseState.value = makeResponseState("done", ResponseState.ACCEPTED)
                _productList.postValue(repository.getLocalProductList(uid,date))
                oldUID.postValue(uid)
            }else{
                if (_productList.value?.size == 0 || (oldUID.value != uid || !oldDate.value.equals(date))) {
                    _responseState.value = makeResponseState("init", ResponseState.INITIAL)
                    repository.getProductList(uid,
                        ApiResponse(
                            {
                            }, {
                                viewModelScope.launch {
                                    repository.upsertProduct(uid,date,it)
                                }
                                _responseState.value = makeResponseState("accepted", ResponseState.ACCEPTED)
                                _productList.postValue(it)
                                oldUID.postValue(uid)
                                oldDate.postValue(date)
                            }, {
                                Log.e(javaClass.simpleName, it)
                                _responseState.value = makeResponseState(it, ResponseState.FAILED)
                            }
                        )
                    )
                }
            }
        }

    }

    fun retry() {
        load()
    }



}