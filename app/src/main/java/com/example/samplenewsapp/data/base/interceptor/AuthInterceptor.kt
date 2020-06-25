package com.example.samplenewsapp.data.base.interceptor

import com.example.samplenewsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    companion object {
        const val QUERY_PARAM_AUTH = "apiKey"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(
                    chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(QUERY_PARAM_AUTH, BuildConfig.API_KEY)
                        .build()
                )
                .build()
        )
    }
}