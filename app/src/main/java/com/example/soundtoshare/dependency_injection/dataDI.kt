package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.external.SharedPreferencesExternal
import com.example.soundtoshare.repositories.UserInfoRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.repositories.VkAPIRepository
import org.koin.dsl.module

val dataModule = module {
    single<VkAPIRepository> {
        VkAPIRepository()
    }

    single<SharedPreferencesRepository> {
        SharedPreferencesRepository(get(), get())
    }

    single<UserInfoRepository> {
        UserInfoRepository(get())
    }

    factory<SharedPreferencesExternal>{
        SharedPreferencesExternal(get())
    }

    single<FirestoreDatabase> {
        FirestoreDatabase()
    }

}