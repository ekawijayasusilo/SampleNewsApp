package com.example.samplenewsapp

import android.app.Application
import org.koin.android.ext.android.startKoin

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }

}