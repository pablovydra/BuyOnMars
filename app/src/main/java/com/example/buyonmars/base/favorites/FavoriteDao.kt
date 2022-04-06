package com.example.buyonmars.base.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    suspend fun getAll(): MutableList<Favorite>

    @Insert
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE marsId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM favorites WHERE marsId = :id")
    suspend fun getById(id: Int): Favorite

    @Query("UPDATE favorites SET marsId=:marsId,url=:url,type=:type,price=:price WHERE id = :id")
    suspend fun update(
        marsId: String,
        url: String,
        type: String,
        price: Double,
        id: Int
    )
}