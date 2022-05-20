package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.fragments.map.LocationUpdateUseCase
import com.example.soundtoshare.fragments.map.UpdateMarkersUseCase
import com.example.soundtoshare.fragments.settings.IncognitoModeUseCase
import org.koin.dsl.module

val domainModule = module {
    single<VkGetDataUseCase> {
        VkGetDataUseCase(vkApiRepository = get())
    }

    single<IncognitoModeUseCase> {
        IncognitoModeUseCase(sharedPreferencesRepository = get())
    }

    factory<LocationUpdateUseCase> {
        LocationUpdateUseCase(context = get(), userInfoRepository = get(), incognitoModeUseCase = get(), vkGetDataUseCase = get())
    }

    factory<UpdateMarkersUseCase> {
        UpdateMarkersUseCase(vkGetDataUseCase = get(), fireStoreDatabase = get())
    }

}