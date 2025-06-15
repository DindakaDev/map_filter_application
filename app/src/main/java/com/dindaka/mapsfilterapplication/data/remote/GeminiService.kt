package com.dindaka.mapsfilterapplication.data.remote

import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailDto
import com.dindaka.mapsfilterapplication.data.remote.dto.CityDetailMainRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun getCityDetail(
        @Body body: CityDetailMainRequest
    ): CityDetailDto
}