package com.example.samplenewsapp

import androidx.room.Room
import com.example.samplenewsapp.data.article.repository.ArticlesRepositoryImpl
import com.example.samplenewsapp.data.article.service.ArticlesService
import com.example.samplenewsapp.data.base.interceptor.AuthInterceptor
import com.example.samplenewsapp.data.category.repository.CategoriesRepositoryImpl
import com.example.samplenewsapp.data.category.service.CategoryDao
import com.example.samplenewsapp.data.source.repository.SourcesRepositoryImpl
import com.example.samplenewsapp.data.source.service.SourcesService
import com.example.samplenewsapp.domain.article.interactor.ArticlesUseCase
import com.example.samplenewsapp.domain.article.repository.ArticlesRepository
import com.example.samplenewsapp.domain.category.interactor.CategoriesUseCase
import com.example.samplenewsapp.domain.category.repository.CategoriesRepository
import com.example.samplenewsapp.domain.source.interactor.SourcesUseCase
import com.example.samplenewsapp.domain.source.repository.SourcesRepository
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewContract
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewPresenter
import com.example.samplenewsapp.presentation.article.presenters.SearchArticleContract
import com.example.samplenewsapp.presentation.article.presenters.SearchArticlePresenter
import com.example.samplenewsapp.presentation.category.presenters.ListCategoryContract
import com.example.samplenewsapp.presentation.category.presenters.ListCategoryPresenter
import com.example.samplenewsapp.presentation.source.presenters.SearchSourceContract
import com.example.samplenewsapp.presentation.source.presenters.SearchSourcePresenter
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val LOGGING_INTERCEPTOR = "LoggingInterceptor"
const val AUTH_INTERCEPTOR = "AuthInterceptor"

val appModule = module {
    single<Interceptor>(LOGGING_INTERCEPTOR) {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    single<Interceptor>(AUTH_INTERCEPTOR) {
        AuthInterceptor()
    }

    single<CallAdapter.Factory> {
        RxJava2CallAdapterFactory.create()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get(LOGGING_INTERCEPTOR))
            .addInterceptor(get(AUTH_INTERCEPTOR))
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addCallAdapterFactory(get())
            .addConverterFactory(get())
            .build()
    }

    single<Gson> {
        Gson()
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_db"
        )
            .createFromAsset("app_db.db")
            .build()
    }

    // Category
    single<CategoryDao> {
        get<AppDatabase>().categoryDao()
    }

    single<CategoriesRepository> {
        CategoriesRepositoryImpl(get())
    }

    single<CategoriesUseCase> {
        CategoriesUseCase(get())
    }

    factory<ListCategoryContract.Presenter> { (view: ListCategoryContract.View) ->
        ListCategoryPresenter(view, get())
    }

    // Source
    single<SourcesService> {
        SourcesService(get(), get())
    }

    single<SourcesRepository> {
        SourcesRepositoryImpl(get())
    }

    single<SourcesUseCase> {
        SourcesUseCase(get())
    }

    factory<SearchSourceContract.Presenter> { (view: SearchSourceContract.View) ->
        SearchSourcePresenter(view, get())
    }

    // Article
    single<ArticlesService> {
        ArticlesService(get(), get())
    }

    single<ArticlesRepository> {
        ArticlesRepositoryImpl(get())
    }

    single<ArticlesUseCase> {
        ArticlesUseCase(get())
    }

    factory<SearchArticleContract.Presenter> { (view: SearchArticleContract.View) ->
        SearchArticlePresenter(view, get())
    }

    // Article Detail
    factory<ArticleDetailWebViewContract.Presenter> { (view: ArticleDetailWebViewContract.View) ->
        ArticleDetailWebViewPresenter(view)
    }
}