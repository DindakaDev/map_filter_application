package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchCitiesUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<StateManager<Unit>> = flow {
        emit(StateManager.Loading)
        val syncResult = repository.getCities()
        if (syncResult.isFailure) {
            emit(StateManager.Error(syncResult.exceptionOrNull()?.message ?: "Unexpected error!"))
            return@flow
        }
        emit(StateManager.Success(Unit))
    }
}
