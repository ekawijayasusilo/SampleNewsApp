package com.example.samplenewsapp.data.source.model

import com.example.samplenewsapp.domain.source.model.Source
import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("sources") val sources: List<SourceResponse>
) {
    fun toSources(): List<Source> {
        return sources.map { it.toSource() }
    }
}