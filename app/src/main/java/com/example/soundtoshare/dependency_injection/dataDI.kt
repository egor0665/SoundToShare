package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.repositories.VkAPIRepository
import org.koin.dsl.module

val dataModule = module {
    single<VkAPIRepository> {
        VkAPIRepository()
    }
}