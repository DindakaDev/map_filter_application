package com.dindaka.mapsfilterapplication.data.persistence.db.city

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dindaka.mapsfilterapplication.data.persistence.db.CITY_TABLE_NAME

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(manifestEntity: CityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT * FROM $CITY_TABLE_NAME LIMIT 1")
    suspend fun getFirstCity(): CityEntity?

    @Query("SELECT * FROM $CITY_TABLE_NAME ORDER BY name desc")
    fun getAllPaging(): PagingSource<Int, CityEntity>
}