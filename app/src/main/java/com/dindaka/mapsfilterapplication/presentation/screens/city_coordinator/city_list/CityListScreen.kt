package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.model.StateManager
import com.dindaka.mapsfilterapplication.presentation.components.ErrorComponent
import com.dindaka.mapsfilterapplication.presentation.components.LoadingComponent
import com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.SharedCityCoordinatorViewModel
import com.dindaka.mapsfilterapplication.presentation.screens.utils.isLandscape

@Composable
fun CityListScreen(
    sharedViewModel: SharedCityCoordinatorViewModel,
    viewModel: CityListViewModel,
    onItemClick: (CityData) -> Unit,
    onDetailItemClick: ((CityData) -> Unit)? = null,
) {
    val syncState by viewModel.syncState.collectAsState()
    when (syncState) {
        StateManager.Loading -> LoadingComponent(R.string.load_string_server)
        is StateManager.Error -> ErrorComponent((syncState as StateManager.Error).message)
        is StateManager.Success -> {
            CitiesListComponent(
                sharedViewModel,
                viewModel,
                onItemClick,
                onDetailItemClick
            )
        }
    }
}

@Composable
fun CitiesListComponent(
    sharedViewModel: SharedCityCoordinatorViewModel,
    viewModel: CityListViewModel,
    onItemClick: (CityData) -> Unit,
    onDetailItemClick: ((CityData) -> Unit)? = null
) {
    val selectedId by sharedViewModel.selectedItem.collectAsState()
    val searchQuery by viewModel.searchText.collectAsState("")
    val onlyFavorite by viewModel.onlyFavorites.collectAsState(false)
    val cities = viewModel.cities.collectAsLazyPagingItems()

    Column(
        Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            InputFilterComponent(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                searchQuery = searchQuery,
                onSearch = { viewModel.onSearchText(it) }
            )
            FavoriteFilterButton(
                onlyFavorite,
                onClick = {
                    viewModel.toggleFavorites()
                }
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(
                count = cities.itemCount,
                key = cities.itemKey { city -> city.id },
                contentType = cities.itemContentType { "City" }) { index: Int ->
                val city = cities[index]
                city?.let {
                    CityItemComponent(
                        selectedId = selectedId,
                        item = city,
                        onItemClick = onItemClick,
                        onDetailItemClick = onDetailItemClick,
                        onFavoriteClick = {
                            viewModel.onSwitchFavoriteState(city)
                        })
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
fun InputFilterComponent(modifier: Modifier, searchQuery: String, onSearch: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { onSearch(it) },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.filter),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CityItemComponent(
    item: CityData,
    onItemClick: (CityData) -> Unit,
    onFavoriteClick: (CityData) -> Unit,
    selectedId: Int?,
    onDetailItemClick: ((CityData) -> Unit)? = null,
) {
    val favoriteColor by animateColorAsState(
        targetValue = if (item.favorite) Color(0xFFD32F2F) else Color(0xFF9E9E9E),
        label = "Favorite Color Animation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (selectedId == item.id)
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.30f)
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = "Card Background Color"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clickable { onItemClick(item) }
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${item.name}, ${item.country}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${item.lat}, ${item.lon}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { onFavoriteClick(item) }
                ) {
                    Icon(
                        imageVector = if (item.favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (item.favorite)
                            stringResource(R.string.remove_from_favorites)
                        else
                            stringResource(R.string.add_to_favorites),
                        tint = favoriteColor
                    )
                }
            }
            if(!isLandscape()) {
                TextButton(
                    onClick = { onDetailItemClick?.invoke(item) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.detail))
                }
            }
        }
    }
}


@Composable
fun FavoriteFilterButton(
    onlyFavorite: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (onlyFavorite) Color(0xFFFFCDD2) else Color(0xFFE0E0E0),
        label = "Background Animation"
    )

    val iconColor by animateColorAsState(
        targetValue = if (onlyFavorite) Color.Red else Color.DarkGray,
        label = "Icon Color Animation"
    )

    val icon = if (onlyFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    val description = if (onlyFavorite) "Filtering favorites" else "Showing all items"

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (onlyFavorite) Color.Red else Color.Gray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = iconColor
        )
    }
}
