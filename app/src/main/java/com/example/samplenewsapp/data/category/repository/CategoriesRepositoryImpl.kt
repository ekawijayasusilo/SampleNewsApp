package com.example.samplenewsapp.data.category.repository

import com.example.samplenewsapp.data.category.model.CategoryEntity
import com.example.samplenewsapp.data.category.service.CategoryDao
import com.example.samplenewsapp.domain.category.repository.CategoriesRepository

class CategoriesRepositoryImpl(private val dao: CategoryDao) : CategoriesRepository {
    override fun getCategories() = dao.getAll()
        .map { categories -> categories.map { categoryEntity -> categoryEntity.toCategory() } }
        .doOnSuccess { CategoryEntity.cacheCategories(it) }
}