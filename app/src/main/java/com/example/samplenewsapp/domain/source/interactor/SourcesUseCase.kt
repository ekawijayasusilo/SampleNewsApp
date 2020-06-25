package com.example.samplenewsapp.domain.source.interactor

import com.example.samplenewsapp.domain.source.model.Source
import com.example.samplenewsapp.domain.source.repository.SourcesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SourcesUseCase(private val repository: SourcesRepository) {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    operator fun invoke(
        categoryId: String,
        onSuccess: (List<Source>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val disposable = repository.getSources(categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        compositeDisposable.add(disposable)
    }

    fun cancel() {
        compositeDisposable.clear()
    }

}