package com.example.samplenewsapp.presentation.source.models

import com.example.samplenewsapp.domain.source.model.Source

data class SourcePageModel(
    val id: String,
    val name: String,
    val category: String,
    val language: String
) {
    companion object {
        fun from(source: Source) = SourcePageModel(
            source.id,
            source.name,
            source.category.name,
            source.language
        )
    }
}