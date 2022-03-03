package com.example.picsum.ui.images

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class ImagesFragment : BaseFragment<FragmentImagesBinding>(R.layout.fragment_images) {

    private val viewModel: ImagesViewModel by viewModels()
    private val imagesAdapter by lazy { ImagesAdapter(viewModel) }
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        binding.rvImages.run {
            adapter = imagesAdapter
            itemAnimator?.let { animator ->
                if (animator is SimpleItemAnimator) {
                    animator.supportsChangeAnimations = false
                }
            }
            adapter = imagesAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { imagesAdapter.retry() },
                footer = LoadStateAdapter { imagesAdapter.retry() }
            )
        }
        addLoadStateAdapter()
        searchImages()
        viewModel.refreshEvent.asLiveData().observe(viewLifecycleOwner, {
            searchImages(true)
        })
        viewModel.retryEvent.asLiveData().observe(viewLifecycleOwner, {
            imagesAdapter.retry()
        })
    }

    private fun searchImages(isRefresh: Boolean = false) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchImages(isRefresh).collectLatest {
                imagesAdapter.submitData(it)
            }
        }
    }

    private fun addLoadStateAdapter() {
        imagesAdapter.addLoadStateListener { loadState ->
            val emptyVisible =
                loadState.refresh is LoadState.NotLoading && imagesAdapter.itemCount == 0
            val listVisible = loadState.source.refresh is LoadState.NotLoading
            val progressVisible = loadState.source.refresh is LoadState.Loading
            val retryVisible = loadState.source.refresh is LoadState.Error
            viewModel.addLoadStateListener(emptyVisible, listVisible, progressVisible, retryVisible)
        }
    }
}