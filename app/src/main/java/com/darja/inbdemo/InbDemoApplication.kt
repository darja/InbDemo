package com.darja.inbdemo

import android.app.Application
import com.darja.inbdemo.di.appModule
import com.darja.inbdemo.di.decisionActivityModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InbDemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Dependency injection
        startKoin {
            androidContext(this@InbDemoApplication)
            modules(listOf(appModule, decisionActivityModule))
        }
    }
}