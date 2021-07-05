package com.edu.alterjuicechat.di.modules

import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Modules {
    private val repoModule = module {
        // get context via androidApplication() or just get() extension function
//        single { RepoModule.provideDatabase(androidApplication()) }
        single { RepoModule.provideAuthRepo() }
        single { RepoModule.provideChatsRepo() }
        single { RepoModule.provideMessagesRepo() }
    }



    val allModules = listOf(repoModule)

}