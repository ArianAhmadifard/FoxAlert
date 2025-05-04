package com.arian.foxalert.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoxApplication : Application(){
    companion object {
        lateinit var instance: FoxApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}