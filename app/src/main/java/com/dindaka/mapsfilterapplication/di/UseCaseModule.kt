package com.dindaka.mapsfilterapplication.di

import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import com.dindaka.mapsfilterapplication.domain.usecase.FetchCitiesUseCase
import com.dindaka.mapsfilterapplication.domain.usecase.GetCitiesPagingUseCase
import com.dindaka.mapsfilterapplication.domain.usecase.GetCityByIdUseCase
import com.dindaka.mapsfilterapplication.domain.usecase.SetFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideFetchCitiesUseCase(repository: CityRepository) = FetchCitiesUseCase(repository)

    @Provides
    fun provideGetCitiesPagingUseCase(repository: CityRepository) = GetCitiesPagingUseCase(repository)

    @Provides
    fun provideSetFavoriteUseCase(repository: CityRepository) = SetFavoriteUseCase(repository)

    @Provides
    fun provideGetCityByIdUseCase(repository: CityRepository) = GetCityByIdUseCase(repository)
}