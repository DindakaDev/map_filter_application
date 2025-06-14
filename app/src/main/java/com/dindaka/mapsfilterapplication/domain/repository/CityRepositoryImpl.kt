package com.dindaka.mapsfilterapplication.domain.repository

import com.dindaka.mapsfilterapplication.data.mappers.toDomain
import com.dindaka.mapsfilterapplication.data.mappers.toEntity
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val cityDao: CityDao
) : CityRepository {
    override suspend fun getCities(): Result<List<CityData>> {
        return try {
            val local = cityDao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) return Result.success(local)
            val remote = api.getCities().map { it.toDomain() }
            cityDao.insertAll(remote.map { it.toEntity() })
            Result.success(remote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
