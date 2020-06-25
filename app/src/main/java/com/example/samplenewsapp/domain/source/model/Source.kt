package com.example.samplenewsapp.domain.source.model

import com.example.samplenewsapp.domain.category.model.Category

data class Source(
    val id: String,
    val name: String,
    val category: Category,
    val language: String
)