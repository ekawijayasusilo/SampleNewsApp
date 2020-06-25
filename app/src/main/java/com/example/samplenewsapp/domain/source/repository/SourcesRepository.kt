package com.example.samplenewsapp.domain.source.repository

import com.example.samplenewsapp.domain.source.model.Source
import io.reactivex.Single

interface SourcesRepository {
    fun getSources(categoryId: String): Single<List<Source>>
}