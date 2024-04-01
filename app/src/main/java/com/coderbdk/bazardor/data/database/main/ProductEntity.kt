package com.coderbdk.bazardor.data.database.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @ColumnInfo(name = "id")
    val uid: Long,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "data")
    val data: String
){
    @ColumnInfo("index")
    @PrimaryKey(autoGenerate = true)
    var index: Long = 0
}
