package com.example.picsum.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picsum.BR

abstract class BasePagingListAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, BasePagingListAdapter.BaseViewHolder<T>>(diffCallback) {

    var listener: Any? = null

    abstract fun setLayoutViewType(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder<T>(parent, viewType)

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.onBindViewHolder(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int = setLayoutViewType()

    open class BaseViewHolder<T : Any>(parent: ViewGroup, viewType: Int) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        ) {

        private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!

        open fun onBindViewHolder(item: T?, listener: Any?) {
            binding.run {
                setVariable(BR.item, item)
                setVariable(BR.listener, listener)
                executePendingBindings()
            }
        }
    }
}
