package com.dindaka.mapsfilterapplication.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias CitiesDto = List<CityDto>

@JsonClass(generateAdapter = true)
data class CityDto(
    val country: String,
    val name: String,
    @Json(name = "_id") val id: Long,
    val coord: LatLngDto
)

@JsonClass(generateAdapter = true)
data class LatLngDto(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
)
