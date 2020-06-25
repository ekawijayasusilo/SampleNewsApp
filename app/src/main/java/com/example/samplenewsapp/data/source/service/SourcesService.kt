package com.example.samplenewsapp.data.source.service

import com.example.samplenewsapp.data.source.model.SourcesErrorResponse
import com.example.samplenewsapp.data.source.model.SourcesResponse
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Retrofit

class SourcesService(retrofit: Retrofit, private val gson: Gson) {
    private val api = retrofit.create(SourcesAPI::class.java)

    fun getSources(categoryId: String): Single<SourcesResponse> {
        return api.getSources(categoryId).map { response ->
            if (response.isSuccessful) {
                response.body()
            } else {
                val error = gson.fromJson(
                    response.errorBody()?.string(),
                    SourcesErrorResponse::class.java
                )

                throw Exception(error.message)
            }
        }
    }
}