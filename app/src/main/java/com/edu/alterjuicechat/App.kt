package com.edu.alterjuicechat

import android.app.Application
import com.edu.alterjuicechat.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(Modules.allModules)
        }
    }
}
