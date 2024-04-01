package com.coderbdk.bazardor.domain.local.room.main

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "product_category", primaryKeys = ["id"])
data class ProductCategoryEntity(
    @ColumnInfo(name = "id")
    val uid: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "imgUrl")
    val imgURL: String
)