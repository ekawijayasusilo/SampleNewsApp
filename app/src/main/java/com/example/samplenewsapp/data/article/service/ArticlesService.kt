package com.example.samplenewsapp.data.article.service

import com.example.samplenewsapp.data.article.model.ArticlesErrorResponse
import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.utils.NotContinuableException
import com.example.samplenewsapp.utils.Result
import com.google.gson.Gson
import retrofit2.Retrofit

class ArticlesService(retrofit: Retrofit, private val gson: Gson) {
    private val api = retrofit.create(ArticlesAPI::class.java)

    companion object {
        const val ERROR_CODE_NOT_CONTINUABLE = "maximumResultsReached"
    }

    suspend fun getArticles(
        searchTerm: String,
        sourceId: String,
        page: Int = 1
    ): Result<List<Article>> {
        return api.getArticles(searchTerm, sourceId, page).let { response ->
            if (response.isSuccessful) {
                if (response.body()?.totalResults != 0) {
                    Result.Success(response.body()!!.toArticles())
                } else {
                    Result.Error(NotContinuableException("No more article found"))
                }
            } else {
                val error = gson.fromJson(
                    response.errorBody()?.string(),
                    ArticlesErrorResponse::class.java
                )

                val exception = if (error.code == ERROR_CODE_NOT_CONTINUABLE) {
                    NotContinuableException(error.message)
                } else {
                    Exception(error.message)
                }

                Result.Error(exception)
            }
        }
    }
}