package com.example.samplenewsapp.data.category.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.samplenewsapp.domain.category.model.Category

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "illustrationUrl")
    val illustrationUrl: String
) {
    companion object {
        private var cachedCategories: Map<String, Category>? = null

        fun cacheCategories(categories: List<Category>) {
            cachedCategories = categories.associateBy { it.name }
        }

        fun getCachedCategory(name: String) =
            cachedCategories?.get(name) ?: createEmptyCategory(name)

        fun createEmptyCategory(name: String = "") = Category("", name, "")
    }

    fun toCategory() = Category(id, name, illustrationUrl)
}