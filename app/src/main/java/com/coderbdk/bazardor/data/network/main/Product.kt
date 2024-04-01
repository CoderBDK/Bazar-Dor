package com.coderbdk.bazardor.data.network.main

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("unit")
    val priceUnit: String,
    @SerializedName("img")
    val imgURL: String
)
