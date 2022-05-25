package com.example.soundtoshare

import android.app.Application
import com.example.soundtoshare.dependency_injection.appModule
import com.example.soundtoshare.dependency_injection.dataModule
import com.example.soundtoshare.dependency_injection.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AppKoin : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@AppKoin)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}
