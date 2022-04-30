package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.HomeViewModel
import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val fragmentDI = module {
    viewModel<HomeViewModel> {
        HomeViewModel(
            vkGetDataUseCase = get()
        )
    }
}