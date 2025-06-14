package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.usecase.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<StateManager<List<CityData>>>(StateManager.Loading)
    val uiState: StateFlow<StateManager<List<CityData>>> = _uiState

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    init {
        getCities()
    }

    private fun getCities() {
        viewModelScope.launch {
            getCitiesUseCase().collect { result ->
                _uiState.value = result
            }
        }
    }

    fun onSearchText(text: String) {
        _searchText.value = text
    }
}