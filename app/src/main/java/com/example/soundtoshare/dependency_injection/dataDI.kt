package com.example.soundtoshare.dependency_injection

import androidx.work.OneTimeWorkRequestBuilder
import com.example.soundtoshare.external.SharedPreferencesExternal
import com.example.soundtoshare.fragments.map.LocationRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.repositories.VkAPIRepository
import com.example.soundtoshare.workers.VkWorker
import org.koin.dsl.module

val dataModule = module {
    single<VkAPIRepository> {
        VkAPIRepository()
    }

    single<SharedPreferencesRepository> {
        SharedPreferencesRepository(get(), get())
    }

    factory<LocationRepository> {
        LocationRepository()
    }
    factory<SharedPreferencesExternal>{
        SharedPreferencesExternal(get())
    }
}