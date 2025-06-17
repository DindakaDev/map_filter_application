package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCityDetailUseCaseTest {

    private lateinit var repository: CityRepository
    private lateinit var useCase: GetCityDetailUseCase

    private val testRequest = CityDetailRequest(
        text = ""
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCityDetailUseCase(repository)
    }

    @Test
    fun `Call api to get detail of city by name and country, success call`() = runTest {
        // Given
        val mockCityDetail = mockk<CityDetailData>()
        coEvery { repository.getCityDetail("Jalisco", "Mexico", testRequest) } returns Result.success(mockCityDetail)

        // When
        val result = useCase("Jalisco", "Mexico", testRequest).toList()

        // Then
        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state is StateManager.Success)
        assertEquals(mockCityDetail, (state as StateManager.Success).data)
    }

    @Test
    fun `Call api to get detail of city with a network connection error`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery {
            repository.getCityDetail("Paris", "France", testRequest)
        } returns Result.failure(Exception(errorMessage))

        // When
        val result = useCase("Paris", "France", testRequest).toList()

        // Then
        assertEquals(1, result.size)
        val state = result.first()
        assertTrue(state is StateManager.Error)
        assertEquals(errorMessage, (state as StateManager.Error).message)
    }
}
