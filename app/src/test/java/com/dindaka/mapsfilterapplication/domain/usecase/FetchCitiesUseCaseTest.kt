package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class FetchCitiesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: CityRepository

    private lateinit var useCase: FetchCitiesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        useCase = FetchCitiesUseCase(repository)
    }

    @Test
    fun `Get cities from api success case`() = runTest {
        // Given
        coEvery { repository.getCities() } returns Result.success(Unit)
        val useCase = FetchCitiesUseCase(repository)

        // When
        val emissions = useCase().toList()

        // Then
        assertEquals(StateManager.Loading, emissions[0])
        assertEquals(StateManager.Success(Unit), emissions[1])
    }

    @Test
    fun `Get cities from api error on api`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getCities() } returns Result.failure(exception)
        val useCase = FetchCitiesUseCase(repository)

        // When
        val emissions = useCase().toList()

        // Then
        assertEquals(StateManager.Loading, emissions[0])
        assertEquals(StateManager.Error("Network error"), emissions[1])
    }

    @Test
    fun `Get cities from api error not returner from api, default message`() = runTest {
        // Given
        coEvery { repository.getCities() } returns Result.failure(Exception())
        val useCase = FetchCitiesUseCase(repository)

        // When
        val emissions = useCase().toList()

        // Then
        assertEquals(StateManager.Loading, emissions[0])
        assertEquals(StateManager.Error("Unexpected error!"), emissions[1])
    }

}