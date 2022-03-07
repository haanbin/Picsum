package com.example.picsum.ui.images

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            val footerAdapter = LoadStateAdapter { imagesAdapter.retry() }
            adapter = imagesAdapter.withLoadStateFooter(
                footer = footerAdapter
            )
            layoutManager = GridLayoutManager(requireContext(), 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == imagesAdapter.itemCount && footerAdapter.itemCount > 0) {
                            3
                        } else {
                            1
                        }
                    }
                }
            }
        }

        viewModel.refreshEvent.asLiveData().observe(viewLifecycleOwner, {
            searchImages(true)
        })
        viewModel.retryEvent.asLiveData().observe(viewLifecycleOwner, {
            imagesAdapter.retry()
        })

        initAdapter()
        searchImages()
    }

    private fun searchImages(isRefresh: Boolean = false) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchImages(isRefresh).collectLatest {
                imagesAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        imagesAdapter.addLoadStateListener { loadState ->
            val emptyVisible =
                loadState.refresh is LoadState.NotLoading && imagesAdapter.itemCount == 0
            val listVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            val progressVisible = loadState.mediator?.refresh is LoadState.Loading
            val retryVisible =
                loadState.mediator?.refresh is LoadState.Error && imagesAdapter.itemCount == 0
            viewModel.addLoadStateListener(emptyVisible, listVisible, progressVisible, retryVisible)
        }
    }
}