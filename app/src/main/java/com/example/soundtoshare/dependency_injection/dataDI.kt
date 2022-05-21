package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.UserInfoRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.repositories.VkAPIRepository
import org.koin.dsl.module

val dataModule = module {
    factory<VkAPIRepository> {
        VkAPIRepository()
    }

    factory<SharedPreferencesRepository> {
        SharedPreferencesRepository(context = get())
    }

    factory<UserInfoRepository> {
        UserInfoRepository(fireStoreDatabase = get())
    }

    factory<FirestoreDatabase> {
        FirestoreDatabase()
    }

    single<CacheRepository>{
        CacheRepository()
    }
}