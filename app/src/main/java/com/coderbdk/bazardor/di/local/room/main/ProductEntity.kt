package com.coderbdk.bazardor.di.local.room.main

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "product", primaryKeys = ["id"])
data class ProductEntity(
    @ColumnInfo(name = "id")
    val uid: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "data")
    val data: String
)
