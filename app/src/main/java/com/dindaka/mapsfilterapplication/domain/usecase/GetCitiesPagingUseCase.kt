package com.dindaka.mapsfilterapplication.domain.usecase

import androidx.paging.PagingData
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesPagingUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(
        search: String = "",
        onlyFavorites: Boolean = false
    ): Flow<PagingData<CityData>> {
        return repository.getCitiesPaging(search, onlyFavorites)
    }
}