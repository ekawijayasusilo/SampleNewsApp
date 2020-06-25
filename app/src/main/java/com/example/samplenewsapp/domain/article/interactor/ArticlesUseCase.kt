package com.example.samplenewsapp.domain.article.interactor

import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.domain.article.repository.ArticlesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArticlesUseCase(private val repository: ArticlesRepository) {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    operator fun invoke(
        searchTerm: String,
        sourceId: String,
        page: Int = 1,
        onSuccess: (List<Article>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val disposable = repository.getArticles(searchTerm, sourceId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        compositeDisposable.add(disposable)
    }

    fun cancel() {
        compositeDisposable.clear()
    }
}