package com.dindaka.mapsfilterapplication.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dindaka.mapsfilterapplication.data.mappers.toDomain
import com.dindaka.mapsfilterapplication.data.mappers.toEntity
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val cityDao: CityDao
) : CityRepository {
    override suspend fun getCities(): Result<Unit> {
        return try {
            val local = cityDao.getFirstCity()
            if (local != null) return Result.success(Unit)
            val remote = api.getCities().map { it.toDomain() }
            cityDao.insertAll(remote.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCitiesPaging(
        search: String,
        onlyFavorites: Boolean
    ): Flow<PagingData<CityData>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                cityDao.getCitiesFiltered(
                    search, onlyFavorites
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
