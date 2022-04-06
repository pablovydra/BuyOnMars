package com.example.buyonmars.base.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @ColumnInfo(name = "marsId") val marsId: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "price") val price: Double,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}


