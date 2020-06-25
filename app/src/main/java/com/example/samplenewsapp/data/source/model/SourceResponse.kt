package com.example.samplenewsapp.data.source.model

import com.example.samplenewsapp.data.category.model.CategoryEntity
import com.example.samplenewsapp.domain.source.model.Source
import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("category") val category: String,
    @SerializedName("language") val language: String,
    @SerializedName("country") val country: String
) {
    companion object {
        fun createEmptySource(name: String = "") =
            Source("", name, CategoryEntity.createEmptyCategory(), "")
    }

    fun toSource() = Source(id, name, CategoryEntity.getCachedCategory(category), language)
}