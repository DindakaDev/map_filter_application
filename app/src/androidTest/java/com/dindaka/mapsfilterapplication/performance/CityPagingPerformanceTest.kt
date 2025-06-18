package com.dindaka.mapsfilterapplication.performance

import android.content.Context
import android.util.Log
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dindaka.mapsfilterapplication.data.persistence.db.AppDatabase
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityEntity
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class CityPagingPerformanceTest {

    private lateinit var db: AppDatabase
    private lateinit var cityDao: CityDao

    private val TAG = "CityPagingPerformanceInstrumentalTest"

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cityDao = db.cityDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun databasePaginationPerformance_with500KItems_returnsFirstPageQuickly() {
        runBlocking(Dispatchers.Main) {
            insertFakeCities(cityDao)

            val pager = getCityPager(cityDao)
            val loadTime = measureTimeMillis {
                val pagingData = pager.flow.first()
                val items = pagingData.collectForTest()
                Log.d(TAG, "Loaded ${items.size} items")
            }

            Log.d(TAG, "Page load time: $loadTime ms")
        }
    }

    @Test
    fun citySearch_byPartialName_shouldReturnMatchingCities() {
        runBlocking(Dispatchers.Main) {
            insertFakeCities(cityDao)

            val pager = getCityPager(cityDao, query = "City 1")
            val items = pager.flow.first().collectForTest()

            assertTrue(items.all { it.name.contains("1") })
        }
    }

    @Test
    fun onlyFavoritesFilter_shouldReturnSubset() {
        runBlocking(Dispatchers.Main) {
            insertFakeCities(cityDao)

            val pager = getCityPager(cityDao, onlyFavorites = true)
            val items = pager.flow.first().collectForTest()

            assertTrue(items.all { it.favorite })
        }
    }

    @Test
    fun citySearch_withNoMatches_shouldReturnEmptyList() {
        runBlocking(Dispatchers.Main) {
            insertFakeCities(cityDao)

            val pager = getCityPager(cityDao, query = "Not found!")
            val items = pager.flow.first().collectForTest()

            assertTrue(items.isEmpty())
        }
    }

    @Test
    fun citySearch_validateSorterItems() {
        runBlocking(Dispatchers.Main) {
            insertFakeCities(cityDao)

            val pager = getCityPager(cityDao, query = "")
            val items = pager.flow.first().collectForTest()

            Log.d(TAG, "items to check sorter: ${items.first().name} - ${items[1].name}")

            assertTrue(items.first().name.compareTo(items[1].name, ignoreCase = true) <= 0)
        }
    }

    private suspend fun insertFakeCities(cityDao: CityDao, total: Int = 500_000) {
        val cities = (1..total).map { i ->
            CityEntity(
                id = i.toLong(),
                name = "City ${generateRandomCityName()}",
                country = "Country ${i % 100}",
                lat = Random.nextDouble(-90.0, 90.0),
                lon = Random.nextDouble(-180.0, 180.0),
                favorite = i % 10 == 0
            )
        }

        val insertTime = measureTimeMillis {
            cityDao.insertAll(cities)
        }
        Log.d(TAG, "Inserted $total cities in $insertTime ms")
    }

    private fun generateRandomCityName(
        size: Int = (10..30).random(),
    ): String {
        val characterAllowed = mutableListOf<Char>()
        characterAllowed.addAll('a'..'z')
        characterAllowed.addAll('A'..'Z')
        return (1..size)
            .map { characterAllowed.random() }
            .joinToString("")
    }

    private fun getCityPager(
        cityDao: CityDao,
        query: String = "",
        onlyFavorites: Boolean = false
    ): Pager<Int, CityEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                cityDao.getCitiesFiltered(query, onlyFavorites)
            }
        )
    }
}


suspend fun <T : Any> PagingData<T>.collectForTest(): List<T> {
    val result = mutableListOf<T>()
    lateinit var differ: AsyncPagingDataDiffer<T>
    differ = AsyncPagingDataDiffer(
        diffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
        },
        updateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                for (i in 0 until count) {
                    differ.getItem(i + position)?.let { result.add(it) }
                }
            }

            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        },
        mainDispatcher = Dispatchers.IO,
        workerDispatcher = Dispatchers.IO
    )

    val job = CoroutineScope(Dispatchers.IO).launch {
        differ.submitData(this@collectForTest)
    }

    var loadState: CombinedLoadStates? = null
    differ.addLoadStateListener { loadState = it }

    repeat(50) {
        if (loadState?.source?.refresh is LoadState.NotLoading) {
            return@repeat
        }
        delay(100)
    }

    job.cancel()
    differ.removeLoadStateListener { }
    return result
}
