package com.example.samplenewsapp.data.article.model

import com.example.samplenewsapp.data.source.model.SourceResponse
import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.utils.removeInstant
import com.example.samplenewsapp.utils.toDate
import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("source") val source: SourceSimpleResponse,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String
) {
    companion object {
        const val PUBLISHED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    fun toArticle() = Article(
        url,
        title,
        urlToImage,
        SourceResponse.createEmptySource(source.name),
        publishedAt.removeInstant().toDate(PUBLISHED_DATE_FORMAT)
    )
}