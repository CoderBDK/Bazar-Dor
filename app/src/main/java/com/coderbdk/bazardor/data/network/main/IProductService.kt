package com.coderbdk.bazardor.data.network.main

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IProductService {
    @FormUrlEncoded
    @POST("product_category.php")
    fun getProductCategoryList(@FieldMap param: MutableMap<String,String>): Call<List<ProductCategory>>

    @FormUrlEncoded
    @POST("product_list.php")
    fun getProductList(@FieldMap param: MutableMap<String,String>): Call<List<Product>>
}