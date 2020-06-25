package com.example.samplenewsapp.presentation.article.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.samplenewsapp.databinding.ActivityArticleDetailWebViewBinding
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewClient
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewContract
import com.example.samplenewsapp.presentation.article.presenters.ArticleDetailWebViewContract.Companion.EXTRA_URL
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class ArticleDetailWebViewActivity : AppCompatActivity(), ArticleDetailWebViewContract.View {

    companion object {
        fun startActivity(activity: Activity, url: String) {
            activity.startActivity(
                Intent(activity, ArticleDetailWebViewActivity::class.java).apply {
                    putExtra(EXTRA_URL, url)
                }
            )
        }
    }

    private val presenter: ArticleDetailWebViewContract.Presenter by inject { parametersOf(this) }
    private lateinit var binding: ActivityArticleDetailWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.attach(intent)
    }

    override fun onDestroy() {
        binding.webView.destroy()
        presenter.detach()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onInitWebView() {
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        binding.webView.webViewClient = ArticleDetailWebViewClient()
    }

    override fun onLoadUrl(url: String) {
        binding.webView.loadUrl(url)
    }
}