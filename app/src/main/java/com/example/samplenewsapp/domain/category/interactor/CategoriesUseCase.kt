package com.example.samplenewsapp.domain.category.interactor

import com.example.samplenewsapp.domain.category.model.Category
import com.example.samplenewsapp.domain.category.repository.CategoriesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoriesUseCase(private val repository: CategoriesRepository) {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    operator fun invoke(
        onSuccess: (List<Category>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val disposable = repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        compositeDisposable.add(disposable)
    }

    fun cancel() {
        compositeDisposable.clear()
    }
}