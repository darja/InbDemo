package com.darja.inbdemo

import android.app.Application
import com.darja.inbdemo.di.app
import com.darja.inbdemo.di.data
import com.darja.inbdemo.di.domain
import com.darja.inbdemo.di.mvvm
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InbDemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Dependency injection
        startKoin {
            androidContext(this@InbDemoApplication)
            modules(listOf(app, data, domain, mvvm))
        }
    }
}