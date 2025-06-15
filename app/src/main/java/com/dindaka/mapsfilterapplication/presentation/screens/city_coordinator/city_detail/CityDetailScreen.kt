package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.CityDetailData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.presentation.components.ErrorComponent
import com.dindaka.mapsfilterapplication.presentation.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
           TopAppBar(
                title = { Text(stringResource(R.string.detail)) },
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
        content = { paddingValues ->
            when (cityDetail) {
                StateManager.Loading -> LoadingComponent(R.string.load_string_city_detail)
                is StateManager.Error -> ErrorComponent((cityDetail as StateManager.Error).message)
                is StateManager.Success -> {
                    CityDetailComponent(modifier = Modifier.padding(paddingValues),(cityDetail as StateManager.Success<CityDetailData?>).data)
                }
            }
        }
    )
}

@Composable
fun CityDetailComponent(modifier: Modifier = Modifier, cityDetail: CityDetailData?) {
    cityDetail?.let {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                cityDetail.image?.let { imageUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.city_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "${cityDetail.stateOrRegion}, ${cityDetail.country}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(label = stringResource(R.string.capital), value = cityDetail.capital)
                DetailRow(label = stringResource(R.string.population), value = "%,d".format(cityDetail.population))
                DetailRow(label = stringResource(R.string.timezone), value = cityDetail.timezone)
                DetailRow(label = stringResource(R.string.currency), value = cityDetail.currency)
                DetailRow(label = stringResource(R.string.language), value = cityDetail.language)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.region_description),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = cityDetail.regionDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.climate),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = cityDetail.climate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.fun_fact),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = cityDetail.funFact,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(2f)
        )
    }
}
