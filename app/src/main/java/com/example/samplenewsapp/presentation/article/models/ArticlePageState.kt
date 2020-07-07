package com.example.samplenewsapp.presentation.article.models

sealed class ArticlePageState {
    class Result(val articles: List<ArticlePageModel>, val isFirstPageResult: Boolean) :
        ArticlePageState()

    class Error(val message: String) : ArticlePageState()
    object LoadPaging : ArticlePageState()
}