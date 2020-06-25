package com.example.samplenewsapp.presentation.article.presenters

import android.content.Intent
import com.example.samplenewsapp.domain.article.interactor.ArticlesUseCase
import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel
import com.example.samplenewsapp.utils.NotContinuableException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchArticlePresenter(
    private var view: SearchArticleContract.View?,
    private val articlesUseCase: ArticlesUseCase
) : SearchArticleContract.Presenter {

    private var sourceId = ""
    private var searchTerm = ""
    private var tempSearchTerm = ""
    private var nextPage = 1

    private var articles = listOf<ArticlePageModel>()
    private var isLoadingNextPage = false
    private var isNextPageAvailable = true

    private val autoSearch = PublishSubject.create<String>()
    private var disposable: Disposable? = null
    private val onLoadSearch: (String) -> Unit = { searchTerm ->
        this.searchTerm = ""
        this.tempSearchTerm = searchTerm
        this.nextPage = 1
        this.isNextPageAvailable = true

        view?.onLoadingSearchResult()
        loadSearchArticle(searchTerm)
    }

    private val onSuccessSearch: (List<Article>) -> Unit = { articles ->
        this.searchTerm = this.tempSearchTerm
        this.nextPage++
        this.articles = articles.map { ArticlePageModel.from(it) }.toMutableList()

        view?.onSetSearchResult(this.articles)
    }

    private val onSuccessNextPage: (List<Article>) -> Unit = { articles ->
        this.nextPage++
        this.articles = this.articles + articles.map { ArticlePageModel.from(it) }
        this.isLoadingNextPage = false

        view?.onSetNextPageResult(this.articles)
    }

    private val onError: (Throwable) -> Unit = { error ->
        if (error is NotContinuableException) {
            isNextPageAvailable = false
            if (this.searchTerm != this.tempSearchTerm) {
                onSuccessSearch(emptyList())
            }
        }
        view?.onSetErrorResult(error.message ?: "")
    }

    override fun attach(intent: Intent?) {
        if (intent != null) {
            sourceId = intent.getStringExtra(SearchArticleContract.EXTRA_SOURCE_ID) ?: ""
            view?.onInitUI()
            initAutoSearch()
            loadSearchArticle("")
        }
    }

    override fun detach() {
        articlesUseCase.cancel()
        view = null
    }

    override fun loadSearch(searchTerm: String) = autoSearch.onNext(searchTerm)

    override fun loadNextPage() {
        if (!isLoadingNextPage && isNextPageAvailable) {
            isLoadingNextPage = true

            view?.onLoadingNextPageResult()
            articlesUseCase(searchTerm, this.sourceId, nextPage, onSuccessNextPage, onError)
        }
    }

    override fun chooseArticle(article: ArticlePageModel) {
        view?.onNavigateToArticleDetail(article.url)
    }

    private fun loadSearchArticle(searchTerm: String) {
        articlesUseCase.cancel()
        articlesUseCase(
            searchTerm,
            this.sourceId,
            this.nextPage,
            this.onSuccessSearch,
            this.onError
        )
    }

    private fun initAutoSearch() {
        disposable = autoSearch.debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onLoadSearch)
    }
}