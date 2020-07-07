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
import com.example.samplenewsapp.presentation.article.models.ArticlePageState
import com.example.samplenewsapp.presentation.article.presenters.SearchArticleViewModel
import com.example.samplenewsapp.utils.PageState
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel


class SearchArticleActivity : AppCompatActivity(), ArticleAdapter.ArticleAdapterListener {

    companion object {

        const val EXTRA_SOURCE_ID = "EXTRA_SOURCE_ID"

        fun startActivity(activity: Activity, sourceId: String) {
            activity.startActivity(
                Intent(activity, SearchArticleActivity::class.java).apply {
                    putExtra(EXTRA_SOURCE_ID, sourceId)
                }
            )
        }
    }

    val viewModel: SearchArticleViewModel by viewModel()
    private val adapter = ArticleAdapter(this)
    private lateinit var binding: ActivitySearchArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initUI()
        initObserver()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initUI() {
        binding.recyclerViewArticle.adapter = adapter
        binding.textInputLayoutSearchArticle.editText?.doAfterTextChanged { s ->
            viewModel.loadSearch(s.toString())
        }
    }

    private fun initObserver() {
        viewModel.liveData.observe(::getLifecycle, ::setupView)
        viewModel.initData(intent.getStringExtra(EXTRA_SOURCE_ID)!!)
    }

    private fun setupView(state: PageState<ArticlePageState>) {
        when (state) {
            PageState.Loading -> showLoading()
            is PageState.Render -> render(state.renderState)
        }
    }

    private fun showLoading() {
        binding.recyclerViewArticle.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.textInputLayoutSearchArticle.isEnabled = false
    }

    private fun render(renderState: ArticlePageState) {
        when (renderState) {
            ArticlePageState.LoadPaging -> showLoadPaging()
            is ArticlePageState.Result -> showResult(
                renderState.articles,
                renderState.isFirstPageResult
            )
            is ArticlePageState.Error -> showError(renderState.message)
        }
    }

    private fun showLoadPaging() {
        binding.linearLayoutLoadNextPage.visibility = View.VISIBLE
    }

    private fun showResult(articles: List<ArticlePageModel>, isFirstPageResult: Boolean) {
        if (isFirstPageResult) {
            showFirstPageResult(articles)
        } else {
            showNextPageResult(articles)
        }
    }

    private fun showFirstPageResult(articles: List<ArticlePageModel>) {
        adapter.updateList(articles)
        binding.recyclerViewArticle.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.textInputLayoutSearchArticle.isEnabled = true
    }

    private fun showNextPageResult(articles: List<ArticlePageModel>) {
        adapter.updateList(articles)
        binding.linearLayoutLoadNextPage.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.recyclerViewArticle.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.linearLayoutLoadNextPage.visibility = View.GONE
        binding.textInputLayoutSearchArticle.isEnabled = true
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onLoadNextPage() {
        viewModel.loadNextPage()
    }

    override fun onItemClicked(url: String) {
        ArticleDetailWebViewActivity.startActivity(this, url)
    }
}