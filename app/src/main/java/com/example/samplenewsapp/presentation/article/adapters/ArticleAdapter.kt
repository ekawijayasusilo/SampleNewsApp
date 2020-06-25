package com.example.samplenewsapp.presentation.article.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.samplenewsapp.R
import com.example.samplenewsapp.databinding.ItemArticleBinding
import com.example.samplenewsapp.presentation.article.models.ArticlePageModel

class ArticleAdapter(private var listener: ArticleAdapterListener?) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ArticlePageModel>() {
        override fun areItemsTheSame(
            oldItem: ArticlePageModel,
            newItem: ArticlePageModel
        ): Boolean =
            oldItem.url == newItem.url

        override fun areContentsTheSame(
            oldItem: ArticlePageModel,
            newItem: ArticlePageModel
        ): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        if (position == differ.currentList.size - 1) {
            listener?.onLoadNextPage()
        }
    }

    fun updateList(articles: List<ArticlePageModel>) = differ.submitList(articles)

    fun detach() {
        listener = null
    }

    class ViewHolder(view: View, private var listener: ArticleAdapterListener?) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemArticleBinding.bind(view)

        fun bind(article: ArticlePageModel) {
            binding.textViewArticleTitle.text = article.title
            binding.textViewArticleSource.text = article.source
            binding.textViewArticleDate.text = article.publishedAt
            Glide.with(itemView)
                .load(article.illustrationUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_24)
                .into(binding.imageViewIllustration)
            binding.root.setOnClickListener { listener?.onItemClicked(article) }
        }
    }

    interface ArticleAdapterListener {
        fun onLoadNextPage()

        fun onItemClicked(article: ArticlePageModel)
    }
}