package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityDetailUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(
        city: String, country: String, request: CityDetailRequest
    ): Flow<StateManager<CityDetailData?>> = flow {
        val response = repository.getCityDetail(city, country, request)
        if (response.isFailure) {
            emit(StateManager.Error(response.exceptionOrNull()?.message ?: "Unexpected error!"))
            return@flow
        }
        val resp = response.getOrNull()
        if(resp != null) {
            emit(StateManager.Success(resp))
        } else {
            emit(StateManager.Error("Unexpected error!"))
        }
    }
}
