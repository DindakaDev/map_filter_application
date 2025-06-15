package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import javax.inject.Inject

class GetCityByIdUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(
        id: Int
    ): CityData?{
        return repository.getCityById(id)
    }
}