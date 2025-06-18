package com.dindaka.mapsfilterapplication.performance

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.persistence.db.AppDatabase
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityEntity
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import com.dindaka.mapsfilterapplication.data.remote.GeminiService
import com.dindaka.mapsfilterapplication.data.remote.PexelService
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import com.dindaka.mapsfilterapplication.domain.repository.CityRepositoryImpl
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.system.measureTimeMillis

@ExperimentalPagingApi
class CityPagingPerformanceTest {

    private lateinit var db: AppDatabase
    private lateinit var cityDao: CityDao
    private lateinit var repository: CityRepository

    @RelaxedMockK
    private lateinit var apiService: ApiService

    @RelaxedMockK
    private lateinit var geminiApi: GeminiService

    @RelaxedMockK
    private lateinit var pexelService: PexelService

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cityDao = db.cityDao()
        repository = CityRepositoryImpl(apiService, geminiApi, pexelService, cityDao)
    }

    @Test
    fun testPagingPerformance() = runBlocking {
        println("Inserting 500,000 cities...")

        val cities = (1..500_000).map { i ->
            CityEntity(
                id = i.toLong(),
                name = "City $i",
                country = "Country ${i % 100}",
                lat = Random.nextDouble(-90.0, 90.0),
                lon = Random.nextDouble(-180.0, 180.0),
                favorite = i % 10 == 0 // 10% favoritos
            )
        }

        val insertTime = measureTimeMillis {
            cityDao.insertAll(cities)
        }
        println("Inserted 500,000 cities in $insertTime ms")

        val pagerFlow = repository.getCitiesPaging(
            search = "City 2",
            onlyFavorites = false
        )

        /*val queryTime = measureTimeMillis {
            val pagingData = pagerFlow.first()
            val items = mutableListOf<CityData>()
            pagingData.collect { items.add(it) }
            println("Loaded ${items.size} items")
        }*/
        //println("Paging query executed in $queryTime ms")
    }

    @After
    fun tearDown() {
        db.close()
    }
}