package com.example.samplenewsapp.data.article.model

import com.example.samplenewsapp.domain.article.model.Article
import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<ArticleResponse>
) {
    fun toArticles(): List<Article> {
        return articles.map { it.toArticle() }
    }
}