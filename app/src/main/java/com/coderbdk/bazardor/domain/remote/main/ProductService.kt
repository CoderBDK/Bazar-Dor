package com.coderbdk.bazardor.domain.remote.main

import com.coderbdk.bazardor.domain.remote.api.ApiResponse
import com.coderbdk.bazardor.domain.remote.api.RemoteApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductService {
    fun apiCallProductCategoryList(
        param: MutableMap<String, String>,
        apiResponse: ApiResponse<List<ProductCategory>>
    ): Call<List<ProductCategory>> {

        val service = RemoteApiService.callApi(IProductService::class.java)
        val call = service.getProductCategoryList(param)

        apiResponse.onLoad("Init")
        call.enqueue(
            object : Callback<List<ProductCategory>> {
                override fun onResponse(
                    call: Call<List<ProductCategory>>,
                    response: Response<List<ProductCategory>>
                ) {
                    if (response.isSuccessful) apiResponse.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<List<ProductCategory>>, t: Throwable) {
                    apiResponse.onError(t.message.toString())
                }

            }

        )
        return call
    }
    fun apiCallProductList(
        param: MutableMap<String, String>,
        apiResponse: ApiResponse<List<Product>>
    ): Call<List<Product>> {

        val service = RemoteApiService.callApi(IProductService::class.java)
        val call = service.getProductList(param)

        apiResponse.onLoad("Init")
        call.enqueue(
            object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if (response.isSuccessful) apiResponse.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    apiResponse.onError(t.message.toString())
                }

            }

        )
        return call
    }
}