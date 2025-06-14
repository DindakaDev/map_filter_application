package com.dindaka.mapsfilterapplication.domain.repository

import androidx.paging.PagingData
import com.dindaka.mapsfilterapplication.data.model.CityData
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun getCities(): Result<Unit>

    fun getCitiesPaging(): Flow<PagingData<CityData>>
}