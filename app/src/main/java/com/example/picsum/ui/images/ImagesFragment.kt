package com.example.picsum.ui.images

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@AndroidEntryPoint
class ImagesFragment : BaseFragment<FragmentImagesBinding>(R.layout.fragment_images) {

    private val viewModel: ImagesViewModel by viewModels()
    private val imagesAdapter by lazy { ImagesAdapter(viewModel) }

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
        }

        lifecycleScope.launch {
            viewModel.images.collectLatest {
                imagesAdapter.submitData(it)
            }
        }
    }
}