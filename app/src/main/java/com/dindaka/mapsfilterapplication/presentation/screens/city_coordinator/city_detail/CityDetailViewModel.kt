package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.dindaka.mapsfilterapplication.domain.usecase.GetCityByIdUseCase
import com.dindaka.mapsfilterapplication.domain.usecase.GetCityDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    val getCityByIdUseCase: GetCityByIdUseCase,
    val getCityDetailUseCase: GetCityDetailUseCase
) : ViewModel() {

    private val _cityDetail = MutableStateFlow<StateManager<CityDetailData?>>(StateManager.Loading)
    val cityDetail: StateFlow<StateManager<CityDetailData?>> = _cityDetail

    private val _city = MutableStateFlow<CityData?>(null)
    val city: StateFlow<CityData?> = _city

    fun getCityById(id: Int?, promp: String) {
        if (id == null) return
        viewModelScope.launch {
            _city.value = getCityByIdUseCase(id)
            getCityDetail(_city.value, promp)
        }
    }

    private fun getCityDetail(city: CityData?, promp: String) {
        city?.let {
            viewModelScope.launch {
                getCityDetailUseCase(
                    it.name,
                    it.country,
                    CityDetailRequest(String.format(promp, it.name, it.country))
                ).collect {
                    _cityDetail.value = it
                }
            }
        }
    }
}