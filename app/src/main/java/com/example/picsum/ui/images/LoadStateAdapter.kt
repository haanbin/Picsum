package com.example.picsum.ui.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.picsum.R
import com.example.picsum.databinding.ItemRoadStateBinding

class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<com.example.picsum.ui.images.LoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }

    open class LoadStateViewHolder(
        private val binding: ItemRoadStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.run {
                retry = this@LoadStateViewHolder.retry
                progressVisible = loadState is LoadState.Loading
                retryVisible = loadState is LoadState.Error
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_road_state, parent, false)
                val binding = ItemRoadStateBinding.bind(view)
                return LoadStateViewHolder(binding, retry)
            }
        }
    }
}