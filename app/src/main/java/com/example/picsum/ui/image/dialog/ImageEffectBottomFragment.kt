package com.example.picsum.ui.image.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.picsum.R
import com.example.picsum.base.BaseBottomFragment
import com.example.picsum.databinding.DialogBottomImageEffectBinding
import com.example.picsum.ui.image.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageEffectBottomFragment :
    BaseBottomFragment<DialogBottomImageEffectBinding>(R.layout.dialog_bottom_image_effect) {

    private val viewModel: ImageEffectViewModel by viewModels()
    private val parentViewModel: ImageViewModel by viewModels(
        ownerProducer = { requireParentFragment().childFragmentManager.primaryNavigationFragment!! }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.applyEvent.asLiveData().observe(viewLifecycleOwner, {
            findNavController().popBackStack()
            parentViewModel.applyEffect(it)
        })
    }
}