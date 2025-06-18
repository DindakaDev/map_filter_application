package com.dindaka.mapsfilterapplication.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDetailDto (
    val candidates: List<Candidate>
)

@JsonClass(generateAdapter = true)
data class Candidate (
    val content: Content,
)

@JsonClass(generateAdapter = true)
data class Content (
    val parts: List<Part>,
)

@JsonClass(generateAdapter = true)
data class Part (
    val text: String
)
