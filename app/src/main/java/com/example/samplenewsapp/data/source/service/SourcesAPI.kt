package com.example.samplenewsapp.data.source.service

import com.example.samplenewsapp.data.source.model.SourcesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SourcesAPI {
    companion object {
        const val PATH = "sources"
        const val QUERY_PARAM_CATEGORY = "category"
    }

    @GET(PATH)
    fun getSources(
        @Query(QUERY_PARAM_CATEGORY) categoryId: String
    ): Single<Response<SourcesResponse>>
}