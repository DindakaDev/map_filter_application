package com.dindaka.mapsfilterapplication.domain.repository

import com.dindaka.mapsfilterapplication.data.model.CityData

interface CityRepository {
    suspend fun getCities(): Result<List<CityData>>
}