package com.example.samplenewsapp.presentation.category.presenters

import com.example.samplenewsapp.presentation.category.models.CategoryPageModel

interface ListCategoryContract {
    interface View {
        fun onInitUI()

        fun onSetSearchResult(categories: List<CategoryPageModel>)

        fun onSetErrorResult(message: String)

        fun onNavigateToSearchSource(categoryId: String)
    }

    interface Presenter {
        fun attach()

        fun detach()

        fun chooseCategory(category: CategoryPageModel)
    }
}