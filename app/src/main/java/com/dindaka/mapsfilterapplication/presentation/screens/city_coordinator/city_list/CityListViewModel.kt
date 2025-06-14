package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.domain.usecase.FetchCitiesUseCase
import com.dindaka.mapsfilterapplication.domain.usecase.GetCitiesPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    fetchCitiesUseCase: FetchCitiesUseCase,
    val getCitiesPagingUseCase: GetCitiesPagingUseCase
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites

    private val _syncState = MutableStateFlow<StateManager<Unit>>(StateManager.Loading)
    val syncState: StateFlow<StateManager<Unit>> = _syncState

    private val _cities = MutableStateFlow<PagingData<CityData>>(PagingData.empty())
    val cities: StateFlow<PagingData<CityData>> = _cities

    init {
        viewModelScope.launch {
            fetchCitiesUseCase().collect { result ->
                _syncState.value = result
            }
        }
        observeCities()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCities() {
        viewModelScope.launch {
            combine(_searchText, _onlyFavorites) { search, onlyFavorites ->
                search to onlyFavorites
            }.flatMapLatest { (search, onlyFavorites) ->
                getCitiesPagingUseCase(
                    search = search,
                    onlyFavorites = onlyFavorites
                )
            }.cachedIn(viewModelScope)
                .collectLatest { _cities.value = it }
        }
    }


    fun onSearchText(text: String) {
        _searchText.value = text
    }
}