package com.dindaka.mapsfilterapplication.data.mappers

import com.dindaka.mapsfilterapplication.data.model.CityData
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityEntity
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailMainRequest
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailRequest
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDto
import com.dindaka.mapsfilterapplication.data.remote.dto.ContentRequest

fun CityDto.toDomain(): CityData = CityData(id.toInt(), name, country, coord.lat, coord.lon, false)

fun CityEntity.toDomain(): CityData = CityData(id.toInt(), name, country, lat, lon, favorite)

fun CityData.toEntity(): CityEntity =
    CityEntity(
        id = id.toLong(),
        name = name,
        country = country,
        lat = lat,
        lon = lon,
        favorite = favorite
    )

fun CityDetailRequest.toDto(): CityDetailMainRequest =
    CityDetailMainRequest(contents = listOf(ContentRequest(parts = listOf(this))))
