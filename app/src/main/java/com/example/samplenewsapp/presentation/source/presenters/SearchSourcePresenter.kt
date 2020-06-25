package com.example.samplenewsapp.presentation.source.presenters

import android.content.Intent
import com.example.samplenewsapp.domain.source.interactor.SourcesUseCase
import com.example.samplenewsapp.domain.source.model.Source
import com.example.samplenewsapp.presentation.source.models.SourcePageModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchSourcePresenter(
    private var view: SearchSourceContract.View?,
    private val sourcesUseCase: SourcesUseCase
) : SearchSourceContract.Presenter {

    private var categoryId = ""
    private var searchTerm = ""
    private var tempSearchTerm = ""

    private var sources = listOf<SourcePageModel>()

    private val autoSearch = PublishSubject.create<String>()
    private var disposableAutoSearch: Disposable? = null
    private val onLoadSearch: (String) -> Unit = { searchTerm ->
        this.searchTerm = ""
        this.tempSearchTerm = searchTerm

        view?.onLoadingSearchResult()
        initFilterBySearchTerm(searchTerm, this.sources)
    }

    private var disposableFilter: Disposable? = null

    private val onSuccess: (List<Source>) -> Unit = { sources ->
        this.sources = sources.map { SourcePageModel.from(it) }.toMutableList()
        view?.onSetSearchResult(this.sources)
    }

    private val onSuccessFilter: (List<SourcePageModel>) -> Unit = { sources ->
        this.searchTerm = this.tempSearchTerm
        view?.onSetSearchResult(sources)
    }

    private val onError: (Throwable) -> Unit = { error ->
        view?.onSetErrorResult(error.message ?: "")
    }

    override fun attach(intent: Intent?) {
        if (intent != null) {
            categoryId = intent.getStringExtra(SearchSourceContract.EXTRA_CATEGORY_ID) ?: ""
            view?.onInitUI()
            sourcesUseCase(
                this.categoryId,
                this.onSuccess,
                this.onError
            )
            initAutoSearch()
        }
    }

    override fun detach() {
        sourcesUseCase.cancel()
        view = null
    }

    override fun loadSearch(searchTerm: String) = autoSearch.onNext(searchTerm)

    override fun chooseSource(source: SourcePageModel) {
        view?.onNavigateToSearchArticle(source.id)
    }

    private fun initAutoSearch() {
        disposableAutoSearch = autoSearch.debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onLoadSearch)
    }

    private fun initFilterBySearchTerm(searchTerm: String, sources: List<SourcePageModel>) {
        disposableFilter?.dispose()
        disposableFilter = Single.fromCallable { filterBySearchTerm(searchTerm, sources) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccessFilter, onError)
    }

    private fun filterBySearchTerm(
        searchTerm: String,
        sources: List<SourcePageModel>
    ): List<SourcePageModel> {
        return sources.filter {
            searchTerm.isEmpty() or it.name.contains(searchTerm, ignoreCase = true)
        }
    }
}