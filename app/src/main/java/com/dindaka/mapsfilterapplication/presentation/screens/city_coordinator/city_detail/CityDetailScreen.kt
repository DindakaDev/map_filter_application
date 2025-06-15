package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.presentation.screens.utils.isLandscape
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    viewModel: CityDetailViewModel = hiltViewModel(),
    cityId: Int?,
    onBack: (() -> Unit)? = null
) {
    LaunchedEffect(key1 = cityId) {
        viewModel.getCityById(cityId)
    }

    BackHandler {
        onBack?.let { it() }
    }
    if (isLandscape()) {
        MapDetail(viewModel = viewModel)
    } else {
        val city by viewModel.city.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("${city?.name ?: ""}, ${city?.country ?: ""}") },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(
                                onClick = it,
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        }
                    }
                )
            },
            content = { padding ->
                MapDetail(Modifier.padding(padding), viewModel = viewModel)
            }
        )
    }
}

@Composable
fun MapDetail(modifier: Modifier = Modifier, viewModel: CityDetailViewModel) {
    val city by viewModel.city.collectAsState()
    val cityLatLng = city?.let { LatLng(it.lat, it.lon) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityLatLng ?: LatLng(0.0, 0.0), 1f)
    }

    LaunchedEffect(cityLatLng) {
        if (cityLatLng != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(cityLatLng, 12f),
                durationMs = 1000
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = true,
            zoomControlsEnabled = true,
            zoomGesturesEnabled = true,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = true,
        ),
    ) {
        if (cityLatLng != null) {
            Marker(
                state = MarkerState(position = cityLatLng),
                title = "${city?.name}, ${city?.country}",
                snippet = "Lat: ${cityLatLng.latitude}, Lon: ${cityLatLng.longitude}"
            )
        }
    }
}