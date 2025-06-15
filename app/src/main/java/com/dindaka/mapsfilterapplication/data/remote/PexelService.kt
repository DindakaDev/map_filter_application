package com.dindaka.mapsfilterapplication.data.remote

import com.dindaka.mapsfilterapplication.data.remote.dto.CityPhotoDetailDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelService {
    @GET("v1/search")
    suspend fun callGetImage(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("orientation") orientation: String,
    ): CityPhotoDetailDto
}