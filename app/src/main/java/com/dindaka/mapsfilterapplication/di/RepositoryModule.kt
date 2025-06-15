package com.dindaka.mapsfilterapplication.di

import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import com.dindaka.mapsfilterapplication.data.remote.GeminiService
import com.dindaka.mapsfilterapplication.data.remote.PexelService
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import com.dindaka.mapsfilterapplication.domain.repository.CityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCityRepository(
        apiService: ApiService,
        geminiService: GeminiService,
        pexelService: PexelService,
        cityDao: CityDao
    ): CityRepository =
        CityRepositoryImpl(apiService, geminiService, pexelService, cityDao)
}