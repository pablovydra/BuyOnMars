package com.example.buyonmars.models.repository

import com.example.buyonmars.models.dto.ApiResource
import com.example.buyonmars.models.dto.MarsProperty
import com.example.buyonmars.models.services.MarsApiService
import javax.inject.Inject

class MarsRepositoryImpl @Inject constructor(private val marsApiService: MarsApiService) :
    BaseRepository(), MarsRepository {

    override suspend fun getProperties(): ApiResource<List<MarsProperty>> {
        return getResult { marsApiService.getProperties() }
    }

}