package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dindaka.mapsfilterapplication.presentation.navigation.Routes
import com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_detail.CityDetailScreen
import com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.map_detail.MapDetailScreen
import com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list.CityListScreen
import com.dindaka.mapsfilterapplication.presentation.screens.utils.isLandscape
import kotlinx.coroutines.launch

@Composable
fun CityCoordinator(viewModel: SharedCityCoordinatorViewModel = hiltViewModel()) {
    if (isLandscape()) {
        LandscapeComponent(viewModel)
    } else {
        PortraitComponent(viewModel)
    }
}

@Composable
fun LandscapeComponent(viewModel: SharedCityCoordinatorViewModel) {
    val selectedId by viewModel.selectedItem.collectAsState()
    val detailSelectedId by viewModel.detailSelectedItem.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                CityListScreen(
                    onItemClick = { city ->
                        scope.launch {
                            viewModel.selectItem(city.id)
                        }
                    },
                    sharedViewModel = viewModel,
                )
            }

            Box(
                modifier = Modifier
                    .weight(2f)
            ) {
                if(detailSelectedId == null) {
                    MapDetailScreen(cityId = selectedId, onDetailItemClick = {
                        scope.launch {
                             viewModel.selectedShowDetail(selectedId)
                        }
                    })
                } else {
                    CityDetailScreen(cityId = selectedId, onBack = {
                        viewModel.selectedShowDetail(null)
                    })
                }
            }
        }
    }
}

@Composable
fun PortraitComponent(viewModel: SharedCityCoordinatorViewModel, fullScreen: Boolean = true) {
    val navController = rememberNavController()
    val selectedId by viewModel.selectedItem.collectAsState()
    val detailSelectedItem by viewModel.detailSelectedItem.collectAsState()

    LaunchedEffect(selectedId) {
        if (selectedId != null) {
            navController.navigate(Routes.Map.createRoute(selectedId!!))
        }
    }

    LaunchedEffect(detailSelectedItem) {
        if (detailSelectedItem != null) {
            navController.navigate(Routes.Detail.createRoute(detailSelectedItem!!))
        }
    }

    NavHost(navController, startDestination = Routes.List.route) {
        composable(Routes.List.route) {
            CityListScreen(
                sharedViewModel = viewModel,
                onItemClick = { city ->
                    viewModel.selectItem(city.id)
                },
                onDetailItemClick = { city ->
                    viewModel.selectedShowDetail(city.id)
                }
            )
        }
        composable(
            route = Routes.Map.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("itemId")
            if (cityId != null) {
                MapDetailScreen(
                    cityId = cityId,
                    onBack = {
                        viewModel.selectItem(null)
                        viewModel.selectedShowDetail(null)
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
        composable(
            route = Routes.Detail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("itemId")
            if (cityId != null) {
                CityDetailScreen(
                    cityId = cityId,
                    onBack = {
                        viewModel.selectItem(null)
                        viewModel.selectedShowDetail(null)
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}