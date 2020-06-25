package com.example.samplenewsapp.presentation.article.presenters

import android.content.Intent
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel

interface SearchArticleContract {
    companion object {
        const val EXTRA_SOURCE_ID = "EXTRA_SOURCE_ID"
    }

    interface View {
        fun onInitUI()

        fun onLoadingSearchResult()

        fun onSetSearchResult(articles: List<ArticlePageModel>)

        fun onLoadingNextPageResult()

        fun onSetNextPageResult(articles: List<ArticlePageModel>)

        fun onSetErrorResult(message: String)

        fun onNavigateToArticleDetail(url: String)
    }

    interface Presenter {
        fun attach(intent: Intent?)

        fun detach()

        fun loadSearch(searchTerm: String)

        fun loadNextPage()

        fun chooseArticle(article: ArticlePageModel)
    }
}