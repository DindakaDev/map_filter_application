package com.dindaka.mapsfilterapplication.domain.usecase

import androidx.paging.PagingData
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetCitiesUseCase(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<PagingData<CityData>> = flow {
        val syncResult = repository.getCities()
        if (syncResult.isFailure) {
            emit(PagingData.empty())
            return@flow
        }
        emitAll(repository.getCitiesPaging())
    }
}
