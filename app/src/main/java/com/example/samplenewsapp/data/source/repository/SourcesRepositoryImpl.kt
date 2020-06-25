package com.example.samplenewsapp.data.source.repository

import com.example.samplenewsapp.data.source.service.SourcesService
import com.example.samplenewsapp.domain.source.repository.SourcesRepository

class SourcesRepositoryImpl(private val service: SourcesService) : SourcesRepository {
    override fun getSources(categoryId: String) =
        service.getSources(categoryId).map { it.toSources() }
}