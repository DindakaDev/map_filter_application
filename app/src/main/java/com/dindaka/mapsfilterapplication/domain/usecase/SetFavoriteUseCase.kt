package com.dindaka.mapsfilterapplication.domain.usecase

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import javax.inject.Inject

class SetFavoriteUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(
        cityData: CityData
    ){
        return repository.updateFavorite(cityData.id, !cityData.favorite)
    }
}