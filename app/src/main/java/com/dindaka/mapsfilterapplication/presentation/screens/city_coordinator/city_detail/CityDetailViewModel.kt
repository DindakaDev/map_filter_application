package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.domain.usecase.GetCityByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    val getCityByIdUseCase: GetCityByIdUseCase
): ViewModel() {

    private val _city = MutableStateFlow<CityData?>(null)
    val city : StateFlow<CityData?> = _city

    fun getCityById(id: Int?){
        if(id == null) return
        viewModelScope.launch {
            _city.value = getCityByIdUseCase(id)
        }
    }
}