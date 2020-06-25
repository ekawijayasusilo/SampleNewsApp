package com.example.samplenewsapp.presentation.category.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.samplenewsapp.databinding.ActivityListCategoryBinding
import com.example.samplenewsapp.presentation.category.adapters.CategoryAdapter
import com.example.samplenewsapp.presentation.category.models.CategoryPageModel
import com.example.samplenewsapp.presentation.category.presenters.ListCategoryContract
import com.example.samplenewsapp.presentation.source.views.SearchSourceActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class ListCategoryActivity : AppCompatActivity(), ListCategoryContract.View,
    CategoryAdapter.CategoryAdapterListener {

    private val presenter: ListCategoryContract.Presenter by inject { parametersOf(this) }
    private val adapter = CategoryAdapter(this)
    private lateinit var binding: ActivityListCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attach()
    }

    override fun onDestroy() {
        presenter.detach()
        adapter.detach()
        super.onDestroy()
    }

    override fun onInitUI() {
        binding.recyclerViewCategory.adapter = adapter
    }

    override fun onSetSearchResult(categories: List<CategoryPageModel>) =
        adapter.updateList(categories)

    override fun onSetErrorResult(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onNavigateToSearchSource(categoryId: String) {
        SearchSourceActivity.startActivity(this, categoryId)
    }

    override fun onItemClicked(category: CategoryPageModel) {
        presenter.chooseCategory(category)
    }
}