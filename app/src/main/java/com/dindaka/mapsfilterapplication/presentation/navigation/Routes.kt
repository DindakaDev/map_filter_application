package com.dindaka.mapsfilterapplication.presentation.navigation

import com.dindaka.mapsfilterapplication.data.model.CityData

sealed class Routes(val route: String) {
    data object List : Routes("list")
    data object Detail : Routes("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}