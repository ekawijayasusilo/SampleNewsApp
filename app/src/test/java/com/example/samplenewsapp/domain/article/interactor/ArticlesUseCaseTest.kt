package com.example.samplenewsapp.domain.article.interactor

import com.example.samplenewsapp.domain.article.model.Article
import com.example.samplenewsapp.domain.article.repository.ArticlesRepository
import com.example.samplenewsapp.utils.NotContinuableException
import com.example.samplenewsapp.utils.Result
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ArticlesUseCaseTest {

    private lateinit var articlesUseCase: ArticlesUseCase

    private lateinit var articlesRepository: ArticlesRepository
    private lateinit var onSuccess: (List<Article>) -> Unit
    private lateinit var onError: (Throwable) -> Unit

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        articlesRepository = mock()
        onSuccess = mock()
        onError = mock()

        articlesUseCase = ArticlesUseCase(articlesRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `given success result, when invoke is called, then should call onSuccess`() {
        runBlocking {
            // given
            val expected = listOf(mock<Article>())
            val result = Result.Success(expected)
            whenever(
                articlesRepository.getArticles(
                    any(),
                    any(),
                    any()
                )
            ).thenReturn(result)

            // when
            articlesUseCase.invoke("", "", 1, onSuccess, onError)

            // then
            verify(articlesRepository).getArticles(any(), any(), any())
            verifyNoMoreInteractions(articlesRepository)
            verify(onSuccess).invoke(eq(expected))
            verifyZeroInteractions(onError)
        }
    }

    @Test
    fun `given error result, when invoke is called, then should call onError`() {
        runBlocking {
            // given
            val expected = NotContinuableException()
            val result = Result.Error(expected)
            whenever(
                articlesRepository.getArticles(
                    any(),
                    any(),
                    any()
                )
            ).thenReturn(result)

            // when
            articlesUseCase.invoke("", "", 1, onSuccess, onError)

            // then
            verify(articlesRepository).getArticles(any(), any(), any())
            verifyNoMoreInteractions(articlesRepository)
            verify(onError).invoke(eq(expected))
            verifyZeroInteractions(onSuccess)
        }
    }
}