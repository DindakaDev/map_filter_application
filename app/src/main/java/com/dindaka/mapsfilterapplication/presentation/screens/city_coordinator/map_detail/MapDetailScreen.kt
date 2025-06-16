package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.map_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.CityData
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
fun MapDetailScreen(
    viewModel: MapDetailViewModel = hiltViewModel(),
    cityId: Int?,
    onDetailItemClick: (()-> Unit)? = null,
    onBack: (() -> Unit)? = null,
) {
    val city by viewModel.city.collectAsState()
    LaunchedEffect(key1 = cityId) {
        viewModel.getCityById(cityId)
    }

    BackHandler {
        onBack?.let { it() }
    }
    if (isLandscape()) {
        MapDetail(viewModel = viewModel, onDetailItemClick = onDetailItemClick)
    } else {
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
                MapDetail(Modifier.padding(padding), viewModel = viewModel, onDetailItemClick)
            }
        )
    }
}

@Composable
fun MapDetail(
    modifier: Modifier = Modifier,
    viewModel: MapDetailViewModel,
    onDetailItemClick: (() -> Unit)?
) {
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
    Box {
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
        if (isLandscape() && city != null) {
            FloatingActionButton(
                onClick = {
                    onDetailItemClick?.invoke()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = 15.dp)
            ) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = stringResource(R.string.detail)
                )
            }
        }
    }
}