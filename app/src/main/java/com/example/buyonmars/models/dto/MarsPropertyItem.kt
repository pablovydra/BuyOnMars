package com.example.buyonmars.models.dto

data class MarsPropertyItem(
    val id: String,
    val url: String,
    val type: String,
    val price: Double,
    var favorite: Boolean = false
)
