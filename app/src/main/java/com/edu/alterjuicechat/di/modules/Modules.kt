package com.edu.alterjuicechat.di.modules

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.NetworkWorker
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Modules {
    private val repoModule = module {
        // get context via androidApplication() or just get() extension function


        single { androidContext().getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }


        single { RepoModule.provideAuthRepo() }
        single { RepoModule.provideChatsRepo() }
        // single { NetworkWorker(Consts.TCP_IP, Consts.TCP_PORT).also { it.start() }}
        single { NetworkWorker(get()) }
        single { RepoModule.provideMessagesRepo() }

        single { Gson() }
        single { UDPWorker(get()) }
        single { TCPWorker(get()) }
    }

    private val viewModelsModule = module {
        viewModel(named("1")) {
            AuthViewModel()
        }
    }





    val allModules = listOf(repoModule, viewModelsModule)

}