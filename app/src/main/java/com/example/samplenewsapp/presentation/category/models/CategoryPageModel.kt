package com.example.samplenewsapp.presentation.category.models

import com.example.samplenewsapp.domain.category.model.Category

data class CategoryPageModel(
    val id: String,
    val name: String,
    val illustrationUrl: String
) {
    companion object {
        fun from(category: Category) = CategoryPageModel(
            category.id,
            category.name,
            category.illustrationUrl
        )
    }
}