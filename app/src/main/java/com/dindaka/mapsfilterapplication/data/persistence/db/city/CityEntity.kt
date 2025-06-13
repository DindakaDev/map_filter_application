package com.dindaka.mapsfilterapplication.data.persistence.db.city

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dindaka.mapsfilterapplication.data.persistence.db.CITY_TABLE_NAME

@Entity(tableName = CITY_TABLE_NAME)
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "favorite") val favorite: Boolean,
)