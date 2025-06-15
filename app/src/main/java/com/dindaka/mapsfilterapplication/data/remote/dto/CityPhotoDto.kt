package com.dindaka.mapsfilterapplication.data.remote.dto

data class CityPhotoDetailDto (
    val page: Long,
    val perPage: Long,
    val photos: List<Photo>?,
    val totalResults: Long,
    val nextPage: String
)

data class Photo (
    val id: Long,
    val width: Long,
    val height: Long,
    val url: String,
    val photographer: String,
    val photographerURL: String,
    val photographerID: Long,
    val avgColor: String,
    val src: Src,
    val liked: Boolean,
    val alt: String
)

data class Src (
    val original: String,
    val large2X: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
