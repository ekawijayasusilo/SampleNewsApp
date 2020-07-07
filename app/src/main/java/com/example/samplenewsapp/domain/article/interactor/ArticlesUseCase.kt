package com.example.samplenewsapp.domain.article.interactor

import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.domain.article.repository.ArticlesRepository
import com.example.samplenewsapp.utils.Result
import kotlinx.coroutines.*

class ArticlesUseCase(private val repository: ArticlesRepository) : CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    operator fun invoke(
        searchTerm: String,
        sourceId: String,
        page: Int = 1,
        onSuccess: (List<Article>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch() {
            val result = withContext(Dispatchers.IO) {
                repository.getArticles(searchTerm, sourceId, page)
            }

            when (result) {
                is Result.Success -> onSuccess(result.data)
                is Result.Error -> onError(result.throwable)
            }
        }
    }

    fun cancel() {
        job.cancelChildren()
    }
}