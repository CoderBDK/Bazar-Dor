package com.coderbdk.bazardor.network.main

import com.google.gson.annotations.SerializedName

data class ProductCategory(
    @SerializedName("id")
    val uid: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val imgURL: String
)