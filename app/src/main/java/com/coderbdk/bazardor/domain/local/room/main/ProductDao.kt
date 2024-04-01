package com.coderbdk.bazardor.domain.local.room.main

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProductDao {

    @Query("SELECT EXISTS(SELECT * FROM product WHERE id = :uid AND date = :date)")
    suspend fun isProductExists(uid : Long, date: String) : Boolean

    @Query("SELECT * FROM product WHERE id IN (:uid) AND date IN (:date)")
    suspend fun getProductList(uid: Long, date: String): ProductEntity
    @Upsert
    suspend fun upsertProduct(productEntity: ProductEntity)
}