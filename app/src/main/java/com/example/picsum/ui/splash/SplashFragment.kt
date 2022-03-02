package com.example.picsum.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.picsum.R
import com.example.picsum.base.BaseFragment
import com.example.picsum.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.navImagesEvent.asLiveData().observe(viewLifecycleOwner) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToImagesFragment())
        }
    }
}
