package com.example.buyonmars.models.repository

import com.example.buyonmars.models.dto.ApiResource
import com.example.buyonmars.models.dto.MarsProperty

interface MarsRepository {

    suspend fun getProperties(): ApiResource<List<MarsProperty>>

}