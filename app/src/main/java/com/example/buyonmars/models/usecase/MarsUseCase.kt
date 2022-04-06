package com.example.buyonmars.models.usecase

import com.example.buyonmars.models.dto.ApiResource
import com.example.buyonmars.models.dto.MarsProperty
import com.example.buyonmars.models.repository.MarsRepositoryImpl
import javax.inject.Inject

class MarsUseCase @Inject constructor(private val marsRepositoryImpl: MarsRepositoryImpl) {

    suspend fun getProperties(): ApiResource<List<MarsProperty>> {
        return marsRepositoryImpl.getProperties()
    }

}