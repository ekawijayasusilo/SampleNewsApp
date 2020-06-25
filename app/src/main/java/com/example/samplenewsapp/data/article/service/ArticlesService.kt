package com.example.samplenewsapp.data.article.service

import com.example.samplenewsapp.data.article.model.ArticlesErrorResponse
import com.example.samplenewsapp.data.article.model.ArticlesResponse
import com.example.samplenewsapp.utils.NotContinuableException
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Retrofit

class ArticlesService(retrofit: Retrofit, private val gson: Gson) {
    private val api = retrofit.create(ArticlesAPI::class.java)

    companion object {
        const val ERROR_CODE_NOT_CONTINUABLE = "maximumResultsReached"
    }

    fun getArticles(
        searchTerm: String,
        sourceId: String,
        page: Int = 1
    ): Single<ArticlesResponse> {
        return api.getArticles(searchTerm, sourceId, page).map { response ->
            if (response.isSuccessful) {
                if (response.body()?.totalResults != 0) {
                    response.body()
                } else {
                    throw NotContinuableException("No more article found")
                }
            } else {
                val error = gson.fromJson(
                    response.errorBody()?.string(),
                    ArticlesErrorResponse::class.java
                )

                throw if (error.code == ERROR_CODE_NOT_CONTINUABLE) {
                    NotContinuableException(error.message)
                } else {
                    Exception(error.message)
                }
            }
        }
    }
}