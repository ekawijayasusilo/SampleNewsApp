package com.example.samplenewsapp.presentation.article.presenters

import android.content.Intent
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewContract.Companion.EXTRA_URL

class ArticleDetailWebViewPresenter(
    private var view: ArticleDetailWebViewContract.View?
) : ArticleDetailWebViewContract.Presenter {

    override fun attach(intent: Intent?) {
        if (intent != null) {
            val url = intent.getStringExtra(EXTRA_URL) as String

            view?.onInitWebView()
            view?.onLoadUrl(url)
        }
    }

    override fun detach() {
        view = null
    }
}