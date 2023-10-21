package com.coderbdk.bazardor.di.local.room.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductCategoryDao {
    @Query("SELECT * FROM product_category")
    suspend fun getProductCategoryList() : List<ProductCategoryEntity>
    @Insert
    suspend fun insertProductCategory(data: ProductCategoryEntity)
}