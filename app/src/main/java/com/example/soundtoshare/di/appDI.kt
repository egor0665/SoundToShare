package com.example.soundtoshare.di

import com.example.soundtoshare.fragments.home.HomeViewModel
import com.example.soundtoshare.fragments.map.MapViewModel
import com.example.soundtoshare.fragments.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<HomeViewModel> {
        HomeViewModel(
            vkGetDataUseCase = get(),
            likedSongsUseCase = get(),
            locationUpdateUseCase = get()
        )
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            incognitoModeUseCase = get(),
            vkGetDataUseCase = get()
        )
    }
    viewModel<MapViewModel> {
        MapViewModel(
            locationUpdateUseCase = get(),
            updateMarkersUseCase = get(),
            moveCameraUseCase = get(),
            likePlayUseCase = get()
        )
    }
}
