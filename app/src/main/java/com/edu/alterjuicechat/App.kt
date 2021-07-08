package com.edu.alterjuicechat

import android.app.Application
import com.edu.alterjuicechat.di.modules.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(Modules.allModules
               //      + module{ getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }
        )
        }
    }
}
