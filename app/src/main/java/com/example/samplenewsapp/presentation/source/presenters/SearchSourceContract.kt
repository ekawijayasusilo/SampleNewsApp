package com.example.samplenewsapp.presentation.source.presenters

import android.content.Intent
import com.example.samplenewsapp.presentation.source.models.SourcePageModel

interface SearchSourceContract {
    companion object {
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
    }

    interface View {
        fun onInitUI()

        fun onLoadingSearchResult()

        fun onSetSearchResult(sources: List<SourcePageModel>)

        fun onSetErrorResult(message: String)

        fun onNavigateToSearchArticle(sourceId: String)
    }

    interface Presenter {
        fun attach(intent: Intent?)

        fun detach()

        fun loadSearch(searchTerm: String)

        fun chooseSource(source: SourcePageModel)
    }
}