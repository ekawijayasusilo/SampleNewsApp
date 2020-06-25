package com.example.samplenewsapp.domain.category.repository

import com.example.samplenewsapp.domain.category.model.Category
import io.reactivex.Single

interface CategoriesRepository {
    fun getCategories(): Single<List<Category>>
}