package com.example.samplenewsapp.domain.article.repository

import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.utils.Result

interface ArticlesRepository {
    suspend fun getArticles(searchTerm: String, sourceId: String, page: Int): Result<List<Article>>
}