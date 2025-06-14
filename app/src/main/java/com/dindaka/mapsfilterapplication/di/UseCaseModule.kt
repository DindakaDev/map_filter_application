package com.dindaka.mapsfilterapplication.di

import com.dindaka.mapsfilterapplication.domain.repository.CityRepository
import com.dindaka.mapsfilterapplication.domain.usecase.GetCitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetCitiesUseCase(repository: CityRepository) = GetCitiesUseCase(repository)
}