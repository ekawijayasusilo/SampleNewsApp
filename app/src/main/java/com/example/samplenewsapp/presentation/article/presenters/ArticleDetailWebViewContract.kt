package com.example.samplenewsapp.presentation.article.presenters

import android.content.Intent
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel

interface ArticleDetailWebViewContract {
    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }

    interface View {
        fun onInitWebView()

        fun onLoadUrl(url: String)
    }

    interface Presenter {
        fun attach(intent: Intent?)

        fun detach()
    }
}