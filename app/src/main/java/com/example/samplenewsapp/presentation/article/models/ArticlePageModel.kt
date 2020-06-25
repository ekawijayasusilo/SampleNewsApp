package com.example.samplenewsapp.presentation.article.models

import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.utils.toString

data class ArticlePageModel(
    val url: String,
    val title: String,
    val illustrationUrl: String,
    val source: String,
    val publishedAt: String
) {
    companion object {
        private const val PUBLISHED_DATE_FORMAT = "dd MMM yy | HH.mm"

        fun from(article: Article) = ArticlePageModel(
            article.url,
            article.title,
            article.illustrationUrl,
            article.source.name,
            article.publishedAt.toString(PUBLISHED_DATE_FORMAT)
        )
    }
}