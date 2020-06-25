package com.example.samplenewsapp.presentation.category.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.samplenewsapp.R
import com.example.samplenewsapp.databinding.ItemCategoryBinding
import com.example.samplenewsapp.presentation.category.models.CategoryPageModel

class CategoryAdapter(private var listener: CategoryAdapterListener?) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CategoryPageModel>() {
        override fun areItemsTheSame(
            oldItem: CategoryPageModel,
            newItem: CategoryPageModel
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CategoryPageModel,
            newItem: CategoryPageModel
        ): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun updateList(categories: List<CategoryPageModel>) = differ.submitList(categories)

    fun detach() {
        listener = null
    }

    class ViewHolder(view: View, private var listener: CategoryAdapterListener?) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(view)

        fun bind(category: CategoryPageModel) {
            binding.textViewName.text = category.name
            Glide.with(itemView)
                .load(category.illustrationUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_24)
                .into(binding.imageViewIllustration)
            binding.root.setOnClickListener { listener?.onItemClicked(category) }
        }
    }

    interface CategoryAdapterListener {
        fun onItemClicked(category: CategoryPageModel)
    }
}