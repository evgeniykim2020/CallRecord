package com.evgeniykim.callrecord

import android.app.Application
import com.evgeniykim.callrecord.di.groupModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

//        startKoin(this@App, listOf(groupModule))

        startKoin {
            androidContext(this@App)
            modules(listOf(groupModule))
        }
    }
}