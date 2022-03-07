package com.example.picsum.ui.image

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentImageBinding
import com.example.picsum.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : BaseFragment<FragmentImageBinding>(R.layout.fragment_image) {

    private val viewModel: ImageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.errorEvent.asLiveData().observe(viewLifecycleOwner, {
            requireContext().showToast(getString(R.string.exception_error))
            findNavController().popBackStack()
        })
        viewModel.backEvent.asLiveData().observe(viewLifecycleOwner, {
            findNavController().popBackStack()
        })
    }
}