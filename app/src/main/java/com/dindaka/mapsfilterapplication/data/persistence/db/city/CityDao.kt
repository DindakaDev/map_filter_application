package com.dindaka.mapsfilterapplication.data.persistence.db.city

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(manifestEntity: CityEntity)
}