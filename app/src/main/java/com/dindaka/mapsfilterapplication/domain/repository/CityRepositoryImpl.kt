package com.dindaka.mapsfilterapplication.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.dindaka.mapsfilterapplication.data.mappers.toDomain
import com.dindaka.mapsfilterapplication.data.mappers.toDto
import com.dindaka.mapsfilterapplication.data.mappers.toEntity
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import com.dindaka.mapsfilterapplication.data.remote.GeminiService
import com.dindaka.mapsfilterapplication.data.remote.PexelService
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val geminiApi: GeminiService,
    private val pexelService: PexelService,
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

    override suspend fun updateFavorite(id: Int, favorite: Boolean) {
        cityDao.updateFavorite(id, favorite)
    }

    override suspend fun getCityById(id: Int): CityData? {
        return cityDao.getCityById(id)?.toDomain()
    }

    override suspend fun getCityDetail(city: String, country: String,cityDetailRequest: CityDetailRequest): Result<CityDetailData> = runCatching {
        coroutineScope {
            val descriptionDeferred = async { geminiApi.getCityDetail(cityDetailRequest.toDto()) }
            val imageDeferred =
                async { pexelService.callGetImage("$city+city+$country+center", 1, 1, "landscape") }

            val description = descriptionDeferred.await()
            val imageUrl = imageDeferred.await()

            val json = description.candidates.first().content.parts.first().text.replace("```json","").replace("```","")
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
            val adapter = moshi.adapter<Map<String, Any>>(type)
            val parsedJson = adapter.fromJson(json)

            CityDetailData(
                image = imageUrl.photos?.firstOrNull()?.src?.medium,
                country = parsedJson?.get("country").toString(),
                stateOrRegion = parsedJson?.get("state_or_region").toString(),
                capital = parsedJson?.get("capital").toString(),
                population = parsedJson?.get("population").toString().toLongOrNull() ?: 0,
                timezone = parsedJson?.get("timezone").toString(),
                currency = parsedJson?.get("currency").toString(),
                language = parsedJson?.get("language").toString(),
                regionDescription = parsedJson?.get("region_description").toString(),
                climate = parsedJson?.get("climate").toString(),
                funFact = parsedJson?.get("fun_fact").toString(),
            )
        }
    }
}
