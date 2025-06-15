package com.dindaka.mapsfilterapplication.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDetailDto (
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata,
    val modelVersion: String,
    val responseId: String
)

@JsonClass(generateAdapter = true)
data class Candidate (
    val content: Content,
    val finishReason: String,
    val avgLogprobs: Double
)

@JsonClass(generateAdapter = true)
data class Content (
    val parts: List<Part>,
    val role: String
)

@JsonClass(generateAdapter = true)
data class Part (
    val text: String
)

@JsonClass(generateAdapter = true)
data class UsageMetadata (
    val promptTokenCount: Long,
    val candidatesTokenCount: Long,
    val totalTokenCount: Long,
    val promptTokensDetails: List<TokensDetail>,
    val candidatesTokensDetails: List<TokensDetail>
)

@JsonClass(generateAdapter = true)
data class TokensDetail (
    val modality: String,
    val tokenCount: Long
)
