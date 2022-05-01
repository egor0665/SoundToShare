package com.example.soundtoshare.dependency_injection

import androidx.work.Worker
import com.example.soundtoshare.fragments.home.HomeViewModel
import com.example.soundtoshare.fragments.map.MapFragmentViewModel
import com.example.soundtoshare.fragments.settings.SettingsViewModel
import com.example.soundtoshare.fragments.settings.SharedPreferenceUseCase
import com.example.soundtoshare.workers.VkWorker
import org.koin.android.ext.koin.androidContext
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
    viewModel<MapFragmentViewModel>{
        MapFragmentViewModel(
            application = get(),
            locationData = get()
        )
    }

//    factory<VkWorker> {
//        VkWorker(androidContext(), get())
//    }
}