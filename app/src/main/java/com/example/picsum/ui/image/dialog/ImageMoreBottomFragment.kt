package com.example.picsum.ui.image.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.picsum.R
import com.example.picsum.base.BaseBottomFragment
import com.example.picsum.databinding.DialogBottomImageMoreBinding
import com.example.picsum.ui.image.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageMoreBottomFragment :
    BaseBottomFragment<DialogBottomImageMoreBinding>(R.layout.dialog_bottom_image_more) {

    private val viewModel: ImageMoreBottomViewModel by viewModels()
    private val parentViewModel: ImageViewModel by viewModels(
        ownerProducer = { requireParentFragment().childFragmentManager.primaryNavigationFragment!! }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.fileDownloadEvent.asLiveData().observe(viewLifecycleOwner, { event ->
            findNavController().popBackStack()
            parentViewModel.onClickFileDownload()
        })
        viewModel.shareEvent.asLiveData().observe(viewLifecycleOwner, { event ->
            findNavController().popBackStack()
            parentViewModel.onClickShare()
        })
        viewModel.webLinkEvent.asLiveData().observe(viewLifecycleOwner, { event ->
            findNavController().popBackStack()
            parentViewModel.onClickWebLink()
        })
        viewModel.effectEvent.asLiveData().observe(viewLifecycleOwner, { event ->
            findNavController().popBackStack()
            parentViewModel.onClickEffect()
        })
    }

}