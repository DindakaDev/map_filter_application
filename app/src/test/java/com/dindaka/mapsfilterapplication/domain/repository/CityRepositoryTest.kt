package com.dindaka.mapsfilterapplication.domain.repository

import com.dindaka.mapsfilterapplication.data.mappers.toDto
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityEntity
import com.dindaka.mapsfilterapplication.data.remote.ApiService
import com.dindaka.mapsfilterapplication.data.remote.GeminiService
import com.dindaka.mapsfilterapplication.data.remote.PexelService
import com.dindaka.mapsfilterapplication.data.remote.dto.Candidate
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailDto
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDto
import com.dindaka.mapsfilterapplication.data.remote.dto.CityPhotoDetailDto
import com.dindaka.mapsfilterapplication.data.remote.dto.Content
import com.dindaka.mapsfilterapplication.data.remote.dto.Part
import com.dindaka.mapsfilterapplication.data.remote.dto.Photo
import com.dindaka.mapsfilterapplication.data.remote.dto.Src
import com.squareup.moshi.JsonEncodingException
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CityRepositoryTest {

    @RelaxedMockK
    private lateinit var apiService: ApiService

    @RelaxedMockK
    private lateinit var geminiApi: GeminiService

    @RelaxedMockK
    private lateinit var pexelService: PexelService

    @RelaxedMockK
    private lateinit var cityDao: CityDao

    private lateinit var repository: CityRepository

    private val testDispatcher = StandardTestDispatcher()

    private val testRequest = CityDetailRequest(text = "")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        repository = CityRepositoryImpl(apiService, geminiApi, pexelService, cityDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success when local data exists`() = runTest {
        // Given
        coEvery { cityDao.getFirstCity() } returns mockk()

        // When
        val result = repository.getCities()

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { apiService.getCities() }
    }

    @Test
    fun `should fetch from remote when no local data exists`() = runTest {
        // Given
        coEvery { cityDao.getFirstCity() } returns null
        val mockCities = listOf(mockk<CityDto>(relaxed = true))
        coEvery { apiService.getCities() } returns mockCities
        coEvery { cityDao.insertAll(any()) } just Runs

        // When
        val result = repository.getCities()

        // Then
        assertTrue(result.isSuccess)
        coVerify { apiService.getCities() }
        coVerify { cityDao.insertAll(any()) }
    }

    @Test
    fun `should return failure when api throws exception`() = runTest {
        // Given
        coEvery { cityDao.getFirstCity() } returns null
        coEvery { apiService.getCities() } throws IOException("Network error")

        // When
        val result = repository.getCities()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `should call dao with correct parameters`() = runTest {
        // Given
        coEvery { cityDao.updateFavorite(any(), any()) } just Runs

        // When
        repository.updateFavorite(1, true)

        // Then
        coVerify { cityDao.updateFavorite(1, true) }
    }

    @Test
    fun `should return null when city not found`() = runTest {
        // Given
        coEvery { cityDao.getCityById(any()) } returns null

        // When
        val result = repository.getCityById(1)

        // Then
        assertNull(result)
    }

    @Test
    fun `should return city when found`() = runTest {
        // Given
        val mockEntity = CityEntity(
            id = 1,
            name = "Colima",
            country = "Mx",
            lat = 0.0,
            lon = 0.0,
            favorite = false
        )
        coEvery { cityDao.getCityById(any()) } returns mockEntity

        // When
        val result = repository.getCityById(1)

        // Then
        assertNotNull(result)
        assertEquals(1, result?.id)
    }

    @Test
    fun `should return city detail when all calls succeed`() = runTest {
        // Given
        val cityDetailDto = CityDetailDto(
            candidates = listOf(
                Candidate(
                    content = Content(
                        parts = listOf(
                            Part(text = "```json${createTestJson()}```")
                        )
                    )
                )
            )
        )

        val cityPhotoDetailDto = CityPhotoDetailDto(
            photos = listOf(
                Photo(src = Src(medium = "test_image_url", original = "test_image_url"))
            )
        )

        coEvery { geminiApi.getCityDetail(any()) } returns cityDetailDto
        coEvery {
            pexelService.callGetImage(
                any(),
                any(),
                any(),
                any()
            )
        } returns cityPhotoDetailDto

        // When
        val result = repository.getCityDetail("Test", "Country", testRequest)

        // Then
        assertTrue(result.isSuccess)
        with(result.getOrNull()!!) {
            assertEquals("test_image_url", image)
            assertEquals("TestCountry", country)
            assertEquals("TestRegion", stateOrRegion)
            assertEquals("TestCapital", capital)
            assertEquals(1000000L, population)
            assertEquals("UTC", timezone)
        }
        coVerify {
            geminiApi.getCityDetail(testRequest.toDto())
            pexelService.callGetImage(
                query = "Test+city+TestCountry+center",
                page = 1,
                perPage = 1,
                orientation = "landscape"
            )
        }
    }

    @Test
    fun `should return failure when gemini call fails`() = runTest {
        // Given
        coEvery { geminiApi.getCityDetail(any()) } throws IOException("API error")

        // When
        val result = repository.getCityDetail("Test", "Country", testRequest)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `should return failure when pexel call fails`() = runTest {
        // Given
        val cityDetailDto = CityDetailDto(
            candidates = listOf(
                Candidate(
                    content = Content(
                        parts = listOf(
                            Part(text = "```json${createTestJson()}```")
                        )
                    )
                )
            )
        )

        coEvery { geminiApi.getCityDetail(any()) } returns cityDetailDto
        coEvery {
            pexelService.callGetImage(
                any(),
                any(),
                any(),
                any()
            )
        } throws IOException("Pexel error")

        // When
        val result = repository.getCityDetail("Test", "Country", testRequest)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `should handle invalid json response`() = runTest {
        // Given
        val cityDetailDto = CityDetailDto(
            candidates = listOf(
                Candidate(
                    content = Content(
                        parts = listOf(
                            Part(text = "invalid json")
                        )
                    )
                )
            )
        )

        coEvery { geminiApi.getCityDetail(any()) } returns cityDetailDto

        // When
        val result = repository.getCityDetail("Test", "Country", testRequest)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is JsonEncodingException)
    }

    @Test
    fun `should handle empty image response`() = runTest {
        // Given
        val cityDetailDto = CityDetailDto(
            candidates = listOf(
                Candidate(
                    content = Content(
                        parts = listOf(
                            Part(text = "```json${createTestJson()}```")
                        )
                    )
                )
            )
        )

        coEvery { geminiApi.getCityDetail(any()) } returns cityDetailDto
        coEvery {
            pexelService.callGetImage(
                any(),
                any(),
                any(),
                any()
            )
        } returns CityPhotoDetailDto(photos = emptyList())

        // When
        val result = repository.getCityDetail("Test", "Country", testRequest)

        // Then
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull()?.image)
    }

    private fun createTestJson(): String {
        return """
            {
                "country": "TestCountry",
                "state_or_region": "TestRegion",
                "capital": "TestCapital",
                "population": "1000000",
                "timezone": "UTC",
                "currency": "USD",
                "language": "English",
                "region_description": "Test description",
                "climate": "Temperate",
                "fun_fWhen": "Test fWhen"
            }
            """.trimIndent()
    }

}
