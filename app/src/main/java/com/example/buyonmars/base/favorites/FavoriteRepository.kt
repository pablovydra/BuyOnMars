package com.example.buyonmars.base.favorites

import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    suspend fun getAll() = favoriteDao.getAll()

    suspend fun insert(favorite: Favorite) = favoriteDao.insert(favorite)

    suspend fun delete(favorite: Favorite) = favoriteDao.delete(favorite)

    suspend fun deleteById(id: Int) = favoriteDao.deleteById(id)

    suspend fun getById(id: Int): Favorite = favoriteDao.getById(id)

    suspend fun update(marsId: String, url: String, type: String, price: Double, id: Int) = favoriteDao.update(
        marsId, url, type, price, id
    )
}