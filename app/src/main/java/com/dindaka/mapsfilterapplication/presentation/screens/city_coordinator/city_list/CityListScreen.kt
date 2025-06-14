package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager

@Composable
fun CityListScreen(
    viewModel: CityListViewModel = hiltViewModel(),
    onItemClick: (CityData) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    CitiesListComponent(
        viewModel,
        onItemClick
    )
    /*when (uiState) {
        Loading -> LoadingComponent()
        is Error -> ErrorComponent((uiState as Error).message)
        is Success -> {
            CitiesListComponent(
                viewModel,
                onItemClick
            )
        }
    }*/
}

@Composable
fun ErrorComponent(message: String) {
    Box(Modifier.fillMaxSize()) {
        Text(message)
    }
}

@Composable
fun CitiesListComponent(
    viewModel: CityListViewModel,
    onItemClick: (CityData) -> Unit
) {
    val cities = viewModel.cities.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchText.observeAsState("")
    Column(
        Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = searchQuery,
            onValueChange = { viewModel.onSearchText(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.onSearchText("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            placeholder = { Text(stringResource(R.string.filter)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(
                count = cities.itemCount,
                key = cities.itemKey { city -> city.id },
                contentType = cities.itemContentType { "City" }) { index: Int ->
                val city = cities[index]
                city?.let {
                    CityItemComponent(city, onItemClick)
                }
            }
            when (cities.loadState.append) {
                is LoadState.Loading -> item { CircularProgressIndicator() }
                is LoadState.Error -> item {
                    val error = cities.loadState.append as LoadState.Error
                    Text("Error de paginación: ${error.error.message}")
                }

                else -> Unit
            }
        }
    }
}

@Composable
fun LoadingComponent() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            Text(
                stringResource(R.string.load_string_server),
                modifier = Modifier.padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CityItemComponent(item: CityData, onItemClick: (CityData) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.name, style = MaterialTheme.typography.titleLarge)
        }
    }
}
