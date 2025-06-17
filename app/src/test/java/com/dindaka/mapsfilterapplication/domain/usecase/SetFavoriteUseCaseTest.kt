package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetFavoriteUseCaseTest{
    @RelaxedMockK
    private lateinit var repository: CityRepository

    private lateinit var useCase: SetFavoriteUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        useCase = SetFavoriteUseCase(repository)
    }

    @Test
    fun `Unmark city to favorite`() = runTest {
        // Given
        val cityData = CityData(id = 1, country = "MX", name = "Mexico", lat = 0.0, lon = 0.0,  favorite = true)

        // When
        useCase.invoke(cityData)

        // Then
        coVerify { repository.updateFavorite(1, false) }
    }

    @Test
    fun `Set city to favorite`() = runTest {
        // Given
        val cityData = CityData(id = 1, country = "MX", name = "Mexico", lat = 0.0, lon = 0.0,  favorite = false)

        // When
        useCase.invoke(cityData)

        // Then
        coVerify { repository.updateFavorite(2, true) }
    }

}