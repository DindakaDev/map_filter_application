package com.dindaka.mapsfilterapplication.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDetailMainRequest (
    val contents: List<ContentRequest>
)

@JsonClass(generateAdapter = true)
data class ContentRequest (
    val parts: List<CityDetailRequest>
)

@JsonClass(generateAdapter = true)
data class CityDetailRequest (
    val text: String,
)
