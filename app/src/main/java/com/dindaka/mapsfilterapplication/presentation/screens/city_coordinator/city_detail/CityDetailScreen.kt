package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.presentation.components.ErrorComponent
import com.dindaka.mapsfilterapplication.presentation.components.LoadingComponent

@Composable
fun CityDetailScreen(
    viewModel: CityDetailViewModel = hiltViewModel(),
    cityId: Int?,
    onBack: (() -> Unit)? = null
) {
    val prompt = stringResource(R.string.location_prompt)
    LaunchedEffect(key1 = cityId) {
        viewModel.getCityById(cityId, prompt)
    }

    BackHandler {
        onBack?.let { it() }
    }
    val cityDetail by viewModel.cityDetail.collectAsState()
    when (cityDetail) {
        StateManager.Loading -> LoadingComponent(R.string.load_string_city_detail)
        is StateManager.Error -> ErrorComponent((cityDetail as Error).message ?: "")
        is StateManager.Success -> {
            Column{}
        }
    }
}