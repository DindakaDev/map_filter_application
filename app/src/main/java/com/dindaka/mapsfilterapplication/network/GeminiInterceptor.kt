package com.dindaka.mapsfilterapplication.network

import com.dindaka.mapsfilterapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GeminiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("key", BuildConfig.GEMINI_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}