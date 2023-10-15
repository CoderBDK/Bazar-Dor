package com.coderbdk.bazardor.di.remote.main

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("unit")
    val priceUnit: String,
    @SerializedName("img")
    val imgURL: String
)
