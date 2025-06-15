package com.dindaka.mapsfilterapplication.domain.repository

import androidx.paging.PagingData
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun getCities(): Result<Unit>

    fun getCitiesPaging(
        search: String,
        onlyFavorites: Boolean,
    ): Flow<PagingData<CityData>>

    suspend fun updateFavorite(
        id: Int,
        favorite: Boolean
    )

    suspend fun getCityById(id: Int): CityData?

    suspend fun getCityDetail(city: String, country: String, cityDetailRequest: CityDetailRequest): Result<CityDetailData>
}