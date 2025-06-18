package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCityByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: CityRepository
    private lateinit var useCase: GetCityByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetCityByIdUseCase(repository)
    }

    @Test
    fun `When city is found, returns city data`() = runTest {
        // Given
        val cityId = 1
        val expectedCity = CityData(
            id = cityId,
            name = "Mexico",
            country = "MX",
            lat = 0.0,
            lon = 0.0,
            favorite = false
        )
        coEvery { repository.getCityById(cityId) } returns expectedCity

        // When
        val result = useCase(cityId)

        // Then
        assertNotNull(result)
        assertEquals(expectedCity, result)
    }

    @Test
    fun `When city is not found, returns null`() = runTest {
        // Given
        val cityId = -1
        coEvery { repository.getCityById(cityId) } returns null

        // When
        val result = useCase(cityId)

        // Then
        assertNull(result)
    }
}