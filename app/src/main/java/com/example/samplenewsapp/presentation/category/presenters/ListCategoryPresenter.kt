package com.example.samplenewsapp.presentation.category.presenters

import com.example.samplenewsapp.domain.category.interactor.CategoriesUseCase
import com.example.samplenewsapp.domain.category.model.Category
import com.example.samplenewsapp.presentation.category.models.CategoryPageModel

class ListCategoryPresenter(
    private var view: ListCategoryContract.View?,
    private val categoriesUseCase: CategoriesUseCase
) : ListCategoryContract.Presenter {

    private var categories = listOf<CategoryPageModel>()

    private val onSuccess: (List<Category>) -> Unit = { categories ->
        this.categories = categories.map { CategoryPageModel.from(it) }.toMutableList()

        view?.onSetSearchResult(this.categories)
    }

    private val onError: (Throwable) -> Unit = { error ->
        view?.onSetErrorResult(error.message ?: "")
    }

    override fun attach() {
        view?.onInitUI()
        categoriesUseCase(onSuccess, onError)
    }

    override fun detach() {
        categoriesUseCase.cancel()
        view = null
    }

    override fun chooseCategory(category: CategoryPageModel) {
        view?.onNavigateToSearchSource(category.id)
    }
}