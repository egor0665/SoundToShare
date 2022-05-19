package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.fragments.map.LocationUpdateUseCase
import com.example.soundtoshare.fragments.map.UpdateMarkersUseCase
import com.example.soundtoshare.fragments.settings.SharedPreferenceUseCase
import org.koin.dsl.module

val domainModule = module {
    single<VkGetDataUseCase> {
        VkGetDataUseCase(vkApiRepository = get())
    }

    single<LocationUpdateUseCase> {
        LocationUpdateUseCase(context = get(), userInfoRepository = get(), sharedPreferenceUseCase = get(), vkGetDataUseCase = get())
    }

    single<SharedPreferenceUseCase> {
        SharedPreferenceUseCase(sharedPreferencesRepository = get())
    }

    single<UpdateMarkersUseCase> {
        UpdateMarkersUseCase(vkGetDataUseCase = get(), fireStoreDatabase = get())
    }

}