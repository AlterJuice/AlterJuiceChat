package com.edu.alterjuicechat.di.modules

import android.content.Context.MODE_PRIVATE
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.socket.UDPWorker
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val NAME_AUTH_REPO_LOCAL = "AuthRepoLocal"
const val NAME_AUTH_REPO_REMOTE = "AuthRepoRemote"
const val NAME_AUTH_REPO_DECORATOR = "AuthRepoDecorator"
const val NAME_CHAT_REPO_DECORATOR = "ChatRepoDecorator"
const val NAME_CHAT_LIST_REPO_DECORATOR = "ChatListRepoDecorator"

object Modules {

    private val thingsModule = module {
        factory { androidContext().getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }
        single { Gson() }
        single { ModulesProvider.getDataStore() }
    }


    private val socketModule = module {
        single { UDPWorker(get(), get()) }
        single { ModulesProvider.getTcpWorker(get(), get()) }
    }


    private val repoAuthModule = module {
        single(named(NAME_AUTH_REPO_LOCAL)) { ModulesProvider.getAuthRepoLocal(get()) }
        single(named(NAME_AUTH_REPO_REMOTE)) { ModulesProvider.getAuthRepoRemote(get(), get()) }
        single(named(NAME_AUTH_REPO_DECORATOR)) {
            ModulesProvider.getAuthRepoDecorator(
                get(),
                get(named(NAME_AUTH_REPO_LOCAL)),
                get(named(NAME_AUTH_REPO_REMOTE))
            )
        }
    }

    private val repoChatListModule = module {
        single(named(NAME_CHAT_LIST_REPO_DECORATOR)) {
            ModulesProvider.getChatListRepoRemote(get())
        }
    }

    private val repoChatModule = module {
        single(named(NAME_CHAT_REPO_DECORATOR)) {
            ModulesProvider.getChatRepoDecorator(
                ModulesProvider.getChatRepoLocal(get()),
                ModulesProvider.getChatRepoRemote(get())
            )
        }
    }

    private val viewModelsModule = module {
        viewModel { ChatListViewModel(get(named(NAME_CHAT_LIST_REPO_DECORATOR)), get()) }
        viewModel { (receiverID: String) ->
            ChatViewModel(get(named(NAME_CHAT_REPO_DECORATOR)), receiverID)
        }
        viewModel { AuthViewModel(get(named(NAME_AUTH_REPO_DECORATOR)), get()) }
    }


    val allModules = listOf(
        thingsModule,
        socketModule,
        repoChatModule,
        repoChatListModule,
        repoAuthModule,
        viewModelsModule
    )
}