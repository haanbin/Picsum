package com.example.picsum.ui.images

import androidx.recyclerview.widget.DiffUtil
import com.example.picsum.R
import com.example.picsum.base.BasePagingListAdapter
import com.example.picsum.data.vo.Image

class ImagesAdapter(viewModel: ImagesViewModel) : BasePagingListAdapter<Image>(DIFF_CALLBACK) {

    init {
        this.listener = viewModel
    }

    override fun setLayoutViewType(): Int = R.layout.item_images

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
                oldItem == newItem
        }
    }
}