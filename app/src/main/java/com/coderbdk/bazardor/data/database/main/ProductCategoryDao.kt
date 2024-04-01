package com.coderbdk.bazardor.data.database.main

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProductCategoryDao {

    @Query("SELECT EXISTS(SELECT * FROM product_category WHERE id = :uid)")
    fun isPCExists(uid : Int) : Boolean

    @Query("SELECT * FROM product_category")
    suspend fun getProductCategoryList() : List<ProductCategoryEntity>
    @Upsert
    suspend fun upsertProductCategory(data: ProductCategoryEntity)

}