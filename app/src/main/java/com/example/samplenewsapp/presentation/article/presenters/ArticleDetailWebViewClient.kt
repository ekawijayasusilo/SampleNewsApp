package com.example.samplenewsapp.presentation.article.presenters

import android.webkit.WebView
import android.webkit.WebViewClient


class ArticleDetailWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?) = false
}