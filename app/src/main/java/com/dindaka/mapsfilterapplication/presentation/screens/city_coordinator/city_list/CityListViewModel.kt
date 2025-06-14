package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.usecase.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    getCitiesUseCase: GetCitiesUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    val cities: Flow<PagingData<CityData>> =
        getCitiesUseCase().cachedIn(viewModelScope)


    fun onSearchText(text: String) {
        _searchText.value = text
    }
}