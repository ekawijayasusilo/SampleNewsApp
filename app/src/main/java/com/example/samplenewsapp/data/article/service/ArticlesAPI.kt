package com.example.samplenewsapp.data.article.service

import com.example.samplenewsapp.data.article.model.ArticlesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesAPI {
    companion object {
        const val PATH = "everything"
        const val QUERY_PARAM_SEARCH_TERM = "q"
        const val QUERY_PARAM_SOURCES = "sources"
        const val QUERY_PARAM_PAGE = "page"
    }

    @GET(PATH)
    suspend fun getArticles(
        @Query(QUERY_PARAM_SEARCH_TERM) searchTerm: String,
        @Query(QUERY_PARAM_SOURCES) sourceId: String,
        @Query(QUERY_PARAM_PAGE) page: Int = 1
    ): Response<ArticlesResponse>
}