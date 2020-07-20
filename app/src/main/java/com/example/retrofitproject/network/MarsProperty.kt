package com.example.retrofitproject.network

import com.squareup.moshi.Json

data class MarsProperty(
    val id: String,
    val type: String,

    @Json(name = "img_src" )
    val imgSrcUrl: String,

    val price: Double
)