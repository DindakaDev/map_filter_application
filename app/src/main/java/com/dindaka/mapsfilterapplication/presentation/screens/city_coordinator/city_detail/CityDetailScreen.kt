package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dindaka.mapsfilterapplication.presentation.screens.utils.isLandscape
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    cityId: Int?,
    onBack: (() -> Unit)? = null
) {
    BackHandler {
        onBack?.let { it() }
    }
    if (isLandscape()) {
        MapDetail(cityId = cityId)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(
                                onClick = it,
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            },
            content = { padding ->
                MapDetail(Modifier.padding(padding), cityId = cityId)
            }
        )
    }
}

@Composable
fun MapDetail(modifier: Modifier = Modifier, cityId: Int?){
    Column(
        modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = {},
            uiSettings = MapUiSettings(
                compassEnabled = true,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = true,
            ),
        ){
        }
    }
}