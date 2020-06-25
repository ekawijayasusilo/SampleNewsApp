package com.example.samplenewsapp.domain.article.repository

import com.example.samplenewsapp.domain.article.model.Article
import io.reactivex.Single

interface ArticlesRepository {
    fun getArticles(searchTerm: String, sourceId: String, page: Int): Single<List<Article>>
}