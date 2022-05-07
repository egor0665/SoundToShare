package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.HomeViewModel
import com.example.soundtoshare.fragments.map.MapViewModel
import com.example.soundtoshare.fragments.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel<HomeViewModel> {
        HomeViewModel(
            vkGetDataUseCase = get()
        )

    }
    viewModel<SettingsViewModel>{
        SettingsViewModel(
            context = get(),
            sharedPreferenceUseCase = get()
        )
    }
    viewModel<MapViewModel>{
        MapViewModel(
            application = get(),
            locationData = get()
        )
    }

//    factory<VkWorker> {
//        VkWorker(androidContext(), get())
//    }
}