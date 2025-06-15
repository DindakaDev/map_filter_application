package com.dindaka.mapsfilterapplication.data.model

data class CityDetailData (
    val image: String?,
    val country: String,
    val stateOrRegion: String,
    val capital: String,
    val population: Long,
    val timezone: String,
    val currency: String,
    val language: String,
    val regionDescription: String,
    val climate: String,
    val funFact: String
)
