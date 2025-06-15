package com.dindaka.workshops_android.di

import com.dindaka.mapsfilterapplication.data.remote.ApiService
import com.dindaka.mapsfilterapplication.data.remote.GeminiService
import com.dindaka.mapsfilterapplication.data.remote.PexelService
import com.dindaka.mapsfilterapplication.network.GeminiInterceptor
import com.dindaka.mapsfilterapplication.network.PexelInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val timeout = 35L

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @GeminiOkHttpClient
    @Provides
    @Singleton
    fun provideGeminiOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(GeminiInterceptor())
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @GeminiRetrofit
    @Provides
    @Singleton
    fun provideGeminiRetrofit(@GeminiOkHttpClient okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
            .build()
    }

    @Singleton
    @Provides
    fun provideGeminiService(@GeminiRetrofit retrofit: Retrofit): GeminiService {
        return retrofit.create(GeminiService::class.java)
    }

    @PexelOkHttpClient
    @Provides
    @Singleton
    fun provideGPexelOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(PexelInterceptor())
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @PexelRetrofit
    @Provides
    @Singleton
    fun providePexelsRetrofit(@PexelOkHttpClient okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
            .build()
    }

    @Singleton
    @Provides
    fun providePexelService(@PexelRetrofit retrofit: Retrofit): PexelService {
        return retrofit.create(PexelService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PexelOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PexelRetrofit
