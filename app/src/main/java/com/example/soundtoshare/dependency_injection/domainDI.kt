package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.fragments.map.LocationUpdateUseCase
import com.example.soundtoshare.fragments.map.UpdateMarkersUseCase
import com.example.soundtoshare.fragments.settings.SharedPreferenceUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<VkGetDataUseCase> {
        VkGetDataUseCase(vkApiRepository = get())
    }

    factory<LocationUpdateUseCase> {
        LocationUpdateUseCase(get(),get(), get(), get())
    }

    factory<SharedPreferenceUseCase> {
        SharedPreferenceUseCase(get(), get())
    }

    factory<UpdateMarkersUseCase> {
        UpdateMarkersUseCase(get(), get())
    }

}