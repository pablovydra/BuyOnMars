package com.example.buyonmars.models.dto

import com.google.gson.annotations.SerializedName

data class MarsProperty(
    val id: String,

    @SerializedName("img_src")
    val url: String,

    val type: String,
    val price: Double
)


