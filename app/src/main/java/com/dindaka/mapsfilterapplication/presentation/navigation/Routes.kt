package com.dindaka.mapsfilterapplication.presentation.navigation

sealed class Routes(val route: String) {
    data object List : Routes("list")
    data object Map : Routes("map/{itemId}") {
        fun createRoute(itemId: Int) = "map/$itemId"
    }
    data object Detail : Routes("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}