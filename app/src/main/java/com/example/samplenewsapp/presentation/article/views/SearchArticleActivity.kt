package com.example.samplenewsapp.presentation.article.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.samplenewsapp.databinding.ActivitySearchArticleBinding
import com.example.samplenewsapp.presentation.article.adapters.ArticleAdapter
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel
import com.example.samplenewsapp.presentation.article.presenters.SearchArticleContract
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class SearchArticleActivity : AppCompatActivity(), SearchArticleContract.View,
    ArticleAdapter.ArticleAdapterListener {

    companion object {
        fun startActivity(activity: Activity, sourceId: String) {
            activity.startActivity(
                Intent(activity, SearchArticleActivity::class.java).apply {
                    putExtra(SearchArticleContract.EXTRA_SOURCE_ID, sourceId)
                }
            )
        }
    }

    private val presenter: SearchArticleContract.Presenter by inject { parametersOf(this) }
    private val adapter = ArticleAdapter(this)
    private lateinit var binding: ActivitySearchArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.attach(intent)
    }

    override fun onDestroy() {
        presenter.detach()
        adapter.detach()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onInitUI() {
        binding.recyclerViewArticle.adapter = adapter
        binding.textInputLayoutSearchArticle.editText?.doAfterTextChanged { s ->
            presenter.loadSearch(s.toString())
        }
    }

    override fun onLoadingSearchResult() {
        binding.recyclerViewArticle.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.textInputLayoutSearchArticle.isEnabled = false
    }

    override fun onSetSearchResult(articles: List<ArticlePageModel>) {
        adapter.updateList(articles)
        binding.recyclerViewArticle.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.textInputLayoutSearchArticle.isEnabled = true
    }

    override fun onLoadingNextPageResult() {
        binding.linearLayoutLoadNextPage.visibility = View.VISIBLE
    }

    override fun onSetNextPageResult(articles: List<ArticlePageModel>) {
        adapter.updateList(articles)
        binding.linearLayoutLoadNextPage.visibility = View.GONE
    }

    override fun onSetErrorResult(message: String) {
        binding.recyclerViewArticle.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.linearLayoutLoadNextPage.visibility = View.GONE
        binding.textInputLayoutSearchArticle.isEnabled = true
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onNavigateToArticleDetail(url: String) {
        ArticleDetailWebViewActivity.startActivity(this, url)
    }

    override fun onLoadNextPage() {
        presenter.loadNextPage()
    }

    override fun onItemClicked(article: ArticlePageModel) {
        presenter.chooseArticle(article)
    }
}