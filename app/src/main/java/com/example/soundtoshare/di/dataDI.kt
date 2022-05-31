package com.example.soundtoshare.di

import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.external.FirestoreDatabase
import com.example.soundtoshare.repositories.CacheRepository
import com.example.soundtoshare.repositories.SharedPreferencesRepository
import com.example.soundtoshare.repositories.UserInfoRepository
import com.example.soundtoshare.repositories.VkAPIRepository
import com.example.soundtoshare.repositories.roomdb.RoomDBRepository
import org.koin.dsl.module

val dataModule = module {
    factory<VkAPIRepository> {
        VkAPIRepository()
    }

    factory<SharedPreferencesRepository> {
        SharedPreferencesRepository(context = get())
    }

    single<RoomDBRepository> {
        RoomDBRepository(context = get())
    }

    factory<UserInfoRepository> {
        UserInfoRepository(fireStoreDatabase = get())
    }

    factory<FirestoreDatabase> {
        FirestoreDatabase()
    }
    factory<FireBaseDatabase> {
        FireBaseDatabase()
    }
    single<CacheRepository> {
        CacheRepository()
    }
}
