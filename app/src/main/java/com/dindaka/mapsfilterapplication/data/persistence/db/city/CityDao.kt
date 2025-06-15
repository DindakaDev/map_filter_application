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

    @Query("SELECT * FROM $CITY_TABLE_NAME WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' AND (:onlyFavorites == 0 OR favorite = 1) ORDER BY name ASC, country ASC")
    fun getCitiesFiltered(query: String, onlyFavorites: Boolean): PagingSource<Int, CityEntity>

    @Query("SELECT * FROM $CITY_TABLE_NAME WHERE id = :id")
    suspend fun getCityById(id: Int): CityEntity?

    @Query("UPDATE $CITY_TABLE_NAME SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, favorite: Boolean)
}