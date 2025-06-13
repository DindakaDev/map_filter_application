package com.dindaka.mapsfilterapplication.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityEntity

@Database(
    entities = [
        CityEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
