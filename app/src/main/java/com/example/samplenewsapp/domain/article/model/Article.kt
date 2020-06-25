package com.example.samplenewsapp.domain.article.model

import com.example.samplenewsapp.domain.source.model.Source
import java.util.*

data class Article(
    val url: String,
    val title: String,
    val illustrationUrl: String,
    val source: Source,
    val publishedAt: Date
)