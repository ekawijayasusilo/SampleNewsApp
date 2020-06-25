package com.example.samplenewsapp.data.article.model

import com.google.gson.annotations.SerializedName

data class ArticlesErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)