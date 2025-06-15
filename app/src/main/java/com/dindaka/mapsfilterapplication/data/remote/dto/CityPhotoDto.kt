package com.dindaka.mapsfilterapplication.data.remote.dto

data class CityPhotoDetailDto (
    val photos: List<Photo>?,
)

data class Photo (
    val src: Src,
)

data class Src (
    val original: String,
    val medium: String,
)
