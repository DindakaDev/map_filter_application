package com.dindaka.mapsfilterapplication.network

import com.dindaka.mapsfilterapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class PexelInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", BuildConfig.PEXEL_API_KEY)
            .build()
        return chain.proceed(request)
    }
}