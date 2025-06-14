package com.dindaka.mapsfilterapplication.data.model

data class CityData(
    val id: Int,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val favorite: Boolean,
)
