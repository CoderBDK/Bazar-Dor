package com.coderbdk.bazardor.domain.repository

import android.annotation.SuppressLint
import com.coderbdk.bazardor.domain.local.room.main.ProductCategoryDao
import com.coderbdk.bazardor.domain.local.room.main.ProductCategoryEntity
import com.coderbdk.bazardor.domain.local.room.main.ProductDao
import com.coderbdk.bazardor.domain.local.room.main.ProductEntity
import com.coderbdk.bazardor.domain.remote.api.ApiResponse
import com.coderbdk.bazardor.domain.remote.main.Product
import com.coderbdk.bazardor.domain.remote.main.ProductCategory
import com.coderbdk.bazardor.domain.remote.main.ProductService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Date

class MainRepository(
    private val productCategoryDao: ProductCategoryDao,
    private val productDao: ProductDao
) {

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("yyyy_MM_dd")

    // remote service
    private val apiService: ProductService = ProductService()

    fun formatTime(timeMillis: Long): String {
        return timeFormat.format(Date(timeMillis))
    }

    fun getProductCategoryList(apiResponse: ApiResponse<List<ProductCategory>>) {

        val map: MutableMap<String, String> = hashMapOf()
        map["api_key"] = "$\\api_key"
        val call = apiService.apiCallProductCategoryList(map, ApiResponse(
            {
                apiResponse.onLoad(it)
            }, {
                apiResponse.onSuccess(it)
            }, {
                apiResponse.onError(it)
            }
        )

        )

    }

    fun getProductList(
        categoryUID: Long,
        apiResponse: ApiResponse<List<Product>>
    ): Call<List<Product>> {
        val map: MutableMap<String, String> = hashMapOf()
        map["api_key"] = "$\\api_key"
        map["category_uid"] = "$categoryUID"
        val call = apiService.apiCallProductList(map, ApiResponse(
            {
                apiResponse.onLoad(it)
            }, {
                apiResponse.onSuccess(it)
            }, {
                apiResponse.onError(it)
            }
        )

        )

        return call
    }

    suspend fun upsertProductCategory(productCategory: ProductCategory) {
        productCategory.apply {
            productCategoryDao.upsertProductCategory(
                ProductCategoryEntity(
                    uid, name, imgURL
                )
            )
        }
    }

    suspend fun getLocalProductCategoryList(): List<ProductCategory> {
        val list = mutableListOf<ProductCategory>()
        productCategoryDao.getProductCategoryList().forEach {
            list.add(ProductCategory(it.uid, it.name, it.imgURL))
        }
        return list
    }

    suspend fun getLocalProductList(uid: Long, date: String): List<Product>{
        val list = mutableListOf<Product>()
        val data = productDao.getProductList(uid,date).data
        val jsonArray = JSONArray(data)
        for(i in 0..<jsonArray.length()){
            val obj = jsonArray.optJSONObject(i)
            list.add(
                Product(
                    obj.optString("name"),
                    obj.optString("price"),
                    obj.optString("unit"),
                    obj.optString("img"),
            ))
        }
        return list
    }

    suspend fun upsertProduct(uid: Long,date: String, list: List<Product>){
        val jsonArray = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("name",it.name)
            obj.put("price",it.price)
            obj.put("unit",it.priceUnit)
            obj.put("img",it.imgURL)
            jsonArray.put(obj)
        }
        productDao.upsertProduct(ProductEntity(uid, date, jsonArray.toString()))
    }

    suspend fun isLocalProductFound(uid: Long, date: String): Boolean {
        return productDao.isProductExists(uid, date)
    }
}