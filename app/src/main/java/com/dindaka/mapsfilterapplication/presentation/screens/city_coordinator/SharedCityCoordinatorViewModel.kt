package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedCityCoordinatorViewModel @Inject constructor() : ViewModel() {
    private val _selectedItem = MutableStateFlow<Int?>(null)
    val selectedItem: StateFlow<Int?> = _selectedItem.asStateFlow()

    private val _detailSelectedItem = MutableStateFlow<Int?>(null)
    val detailSelectedItem: StateFlow<Int?> = _detailSelectedItem.asStateFlow()

    fun selectItem(itemId: Int?) {
        _selectedItem.value = itemId
    }

    fun selectedShowDetail(itemId: Int?) {
        _detailSelectedItem.value = itemId
    }
}