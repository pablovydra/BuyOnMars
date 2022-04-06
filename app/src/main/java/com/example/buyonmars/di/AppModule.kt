package com.example.buyonmars.di

import com.example.buyonmars.models.repository.MarsRepositoryImpl
import com.example.buyonmars.models.services.MarsApiService
import com.example.buyonmars.models.usecase.MarsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://mars.udacity.com/")
        .build()

    @Singleton
    @Provides
    fun provideMarsApiService(retrofit: Retrofit): MarsApiService = retrofit.create(MarsApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(marsApiService: MarsApiService) = MarsRepositoryImpl(marsApiService)

    @Singleton
    @Provides
    fun provideUsecase(marsRepository: MarsRepositoryImpl) = MarsUseCase(marsRepository)

}