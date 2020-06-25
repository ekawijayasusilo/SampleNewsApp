package com.example.samplenewsapp.presentation.source.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.samplenewsapp.databinding.ActivitySearchSourceBinding
import com.example.samplenewsapp.presentation.article.views.SearchArticleActivity
import com.example.samplenewsapp.presentation.source.adapters.SourceAdapter
import com.example.samplenewsapp.presentation.source.models.SourcePageModel
import com.example.samplenewsapp.presentation.source.presenters.SearchSourceContract
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class SearchSourceActivity : AppCompatActivity(), SearchSourceContract.View,
    SourceAdapter.SourceAdapterListener {

    companion object {
        fun startActivity(activity: Activity, categoryId: String) {
            activity.startActivity(
                Intent(activity, SearchSourceActivity::class.java).apply {
                    putExtra(SearchSourceContract.EXTRA_CATEGORY_ID, categoryId)
                }
            )
        }
    }

    private val presenter: SearchSourceContract.Presenter by inject { parametersOf(this) }
    private val adapter = SourceAdapter(this)
    private lateinit var binding: ActivitySearchSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSourceBinding.inflate(layoutInflater)
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
        binding.recyclerViewSource.adapter = adapter
        binding.textInputLayoutSearchSource.editText?.doAfterTextChanged { s ->
            presenter.loadSearch(s.toString())
        }
    }

    override fun onLoadingSearchResult() {
        binding.recyclerViewSource.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.textInputLayoutSearchSource.isEnabled = false
    }

    override fun onSetSearchResult(sources: List<SourcePageModel>) {
        adapter.updateList(sources)
        binding.recyclerViewSource.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.textInputLayoutSearchSource.isEnabled = true
    }

    override fun onSetErrorResult(message: String) {
        binding.recyclerViewSource.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.textInputLayoutSearchSource.isEnabled = true
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onNavigateToSearchArticle(sourceId: String) {
        SearchArticleActivity.startActivity(this, sourceId)
    }

    override fun onItemClicked(source: SourcePageModel) {
        presenter.chooseSource(source)
    }
}