package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCitiesUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<StateManager<List<CityData>>> = flow {
        emit(StateManager.Loading)
        val result = repository.getCities()
        result
            .onSuccess { emit(StateManager.Success(it)) }
            .onFailure { e -> emit(StateManager.Error(e.message ?: "Unknown error")) }
    }.flowOn(Dispatchers.IO)
}
