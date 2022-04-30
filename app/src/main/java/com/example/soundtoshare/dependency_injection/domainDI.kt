package com.example.soundtoshare.dependency_injection

import com.example.soundtoshare.fragments.home.VkGetDataUseCase
import com.example.soundtoshare.repositories.VkAPIRepository
import org.koin.core.scope.get
import org.koin.dsl.module

val domainDI = module {
    single<VkGetDataUseCase> {
        VkGetDataUseCase(vkApiRepository = get())
    }
}