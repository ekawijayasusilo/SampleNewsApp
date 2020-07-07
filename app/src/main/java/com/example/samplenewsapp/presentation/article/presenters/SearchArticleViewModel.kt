package com.example.samplenewsapp.presentation.article.presenters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samplenewsapp.domain.article.interactor.ArticlesUseCase
import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel
import com.example.samplenewsapp.presentation.article.models.ArticlePageState
import com.example.samplenewsapp.utils.NotContinuableException
import com.example.samplenewsapp.utils.PageState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchArticleViewModel(
    private val articlesUseCase: ArticlesUseCase
) : ViewModel() {

    private val _liveData = MutableLiveData<PageState<ArticlePageState>>()
    val liveData: LiveData<PageState<ArticlePageState>>
        get() = _liveData

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

        _liveData.value = PageState.Loading
        loadSearchArticle(searchTerm)
    }

    private val onSuccessSearch: (List<Article>) -> Unit = { articles ->
        this.searchTerm = this.tempSearchTerm
        this.nextPage++
        this.articles = articles.map { ArticlePageModel.from(it) }.toMutableList()

        _liveData.postValue(
            PageState.Render(
                ArticlePageState.Result(
                    this.articles,
                    isFirstPageResult = true
                )
            )
        )
    }

    private val onSuccessNextPage: (List<Article>) -> Unit = { articles ->
        this.nextPage++
        this.articles = this.articles + articles.map { ArticlePageModel.from(it) }
        this.isLoadingNextPage = false

        _liveData.postValue(
            PageState.Render(
                ArticlePageState.Result(
                    this.articles,
                    isFirstPageResult = false
                )
            )
        )
    }

    private val onError: (Throwable) -> Unit = { error ->
        if (error is NotContinuableException) {
            isNextPageAvailable = false
            if (this.searchTerm != this.tempSearchTerm) {
                onSuccessSearch(emptyList())
            }
        }
        _liveData.postValue(
            PageState.Render(
                ArticlePageState.Error(error.message ?: "")
            )
        )
    }

    fun initData(sourceId: String) {
        this.sourceId = sourceId
        initAutoSearch()
        loadSearchArticle("")
    }

    override fun onCleared() {
        articlesUseCase.cancel()
        super.onCleared()
    }

    fun loadSearch(searchTerm: String) = autoSearch.onNext(searchTerm)

    fun loadNextPage() {
        if (!isLoadingNextPage && isNextPageAvailable) {
            isLoadingNextPage = true

            _liveData.value = PageState.Render(ArticlePageState.LoadPaging)
            articlesUseCase(searchTerm, this.sourceId, nextPage, onSuccessNextPage, onError)
        }
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