package com.example.buyonmars.models.services

import com.example.buyonmars.models.dto.MarsProperty
import retrofit2.Response
import retrofit2.http.GET

interface MarsApiService {

    @GET("realestate")
    suspend fun getProperties(): Response<List<MarsProperty>>

}