package com.dindaka.mapsfilterapplication.di

import android.content.Context
import androidx.room.Room
import com.dindaka.mapsfilterapplication.data.persistence.db.AppDatabase
import com.dindaka.mapsfilterapplication.data.persistence.db.city.CityDao
import com.dindaka.mapsfilterapplication.data.persistence.db.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCityDao(appDatabase: AppDatabase): CityDao {
        return appDatabase.cityDao()
    }
}