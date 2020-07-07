package com.example.samplenewsapp.data.article.repository

import com.example.samplenewsapp.data.article.service.ArticlesService
import com.example.samplenewsapp.domain.article.repository.ArticlesRepository

class ArticlesRepositoryImpl(private val service: ArticlesService) : ArticlesRepository {
    override suspend fun getArticles(
        searchTerm: String,
        sourceId: String,
        page: Int
    ) = service.getArticles(searchTerm, sourceId, page)
}