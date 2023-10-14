package com.coderbdk.bazardor.di.repository

import com.coderbdk.bazardor.di.remote.api.ApiResponse
import com.coderbdk.bazardor.di.remote.main.ProductCategory
import com.coderbdk.bazardor.di.remote.main.ProductService
import retrofit2.Call

class MainRepository{

    // remote service
    private val apiService: ProductService = ProductService()
    fun getProductCategoryList(apiResponse: ApiResponse<List<ProductCategory>>): Call<List<ProductCategory>> {
        val map: MutableMap<String,String> = hashMapOf()
        map["api_key"] = "$\\api_key"
        val call = apiService.apiCallProductCategoryList(map, ApiResponse(
            {
                apiResponse.onLoad(it)
            },{
                apiResponse.onSuccess(it)
            },{
                apiResponse.onError(it)
            }
        )

        )

        return call
    }

}