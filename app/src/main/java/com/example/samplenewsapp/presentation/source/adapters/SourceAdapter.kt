package com.example.samplenewsapp.presentation.source.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenewsapp.R
import com.example.samplenewsapp.databinding.ItemSourceBinding
import com.example.samplenewsapp.presentation.source.models.SourcePageModel

class SourceAdapter(private var listener: SourceAdapterListener?) :
    RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<SourcePageModel>() {
        override fun areItemsTheSame(
            oldItem: SourcePageModel,
            newItem: SourcePageModel
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SourcePageModel,
            newItem: SourcePageModel
        ): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_source, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun updateList(sources: List<SourcePageModel>) = differ.submitList(sources)

    fun detach() {
        listener = null
    }

    class ViewHolder(view: View, private var listener: SourceAdapterListener?) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemSourceBinding.bind(view)

        fun bind(source: SourcePageModel) {
            binding.textViewSourceName.text = source.name
            binding.textViewSourceCategory.text = source.category
            binding.textViewSourceLanguage.text = source.language
            binding.root.setOnClickListener { listener?.onItemClicked(source) }
        }
    }

    interface SourceAdapterListener {
        fun onItemClicked(source: SourcePageModel)
    }
}