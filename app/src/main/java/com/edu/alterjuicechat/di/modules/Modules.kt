 package com.edu.alterjuicechat.di.modules

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.repo.DataStore
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.repo.AuthRepo
import com.edu.alterjuicechat.repo.ChatListRepo
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.repo.decorators.AuthRepoDecorator
import com.edu.alterjuicechat.repo.decorators.ChatRepoDecorator
import com.edu.alterjuicechat.repo.localImpl.AuthRepoLocal
import com.edu.alterjuicechat.repo.localImpl.ChatRepoLocal
import com.edu.alterjuicechat.repo.remoteImpl.AuthRepoRemote
import com.edu.alterjuicechat.repo.remoteImpl.ChatListRepoRemote
import com.edu.alterjuicechat.repo.remoteImpl.ChatRepoRemote
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

    private val thingsModule = module{
        factory { androidContext().getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }
        single { Gson() }
        single { DataStore() }
    }

    private val UDPAndTCPWorkersModule = module{
        single { UDPWorker(get(), get()) }
        single { TCPWorker(get(), get()) }
    }

    private fun getAuthRepoLocal(profilePreferences: SharedPreferences): AuthRepo = AuthRepoLocal(profilePreferences)
    private fun getAuthRepoRemote(tcpWorker: TCPWorker, udpWorker: UDPWorker): AuthRepo = AuthRepoRemote(tcpWorker, udpWorker)
    private fun getAuthRepoDecorator(dataStore: DataStore, localAuthRepo: AuthRepo, remoteAuthRepo: AuthRepo): AuthRepo = AuthRepoDecorator(dataStore, localAuthRepo, remoteAuthRepo)

    private fun getChatListRepoRemote(tcpWorker: TCPWorker): ChatListRepo = ChatListRepoRemote(tcpWorker)

    private fun getChatRepoLocal(dataStore: DataStore): ChatRepo = ChatRepoLocal(dataStore)
    private fun getChatRepoRemote(tcpWorker: TCPWorker): ChatRepo = ChatRepoRemote(tcpWorker)
    private fun getChatRepoDecorator(localChatRepo: ChatRepo, remoteChatRepo: ChatRepo): ChatRepo = ChatRepoDecorator(localChatRepo, remoteChatRepo)



    private val authRepoModule = module{
        single(named(NAME_AUTH_REPO_LOCAL)) { getAuthRepoLocal(get()) }
        single(named(NAME_AUTH_REPO_REMOTE)) { getAuthRepoRemote(get(), get()) }
        single(named(NAME_AUTH_REPO_DECORATOR)) {
            getAuthRepoDecorator(
                get(),
                get(named(NAME_AUTH_REPO_LOCAL)),
                get(named(NAME_AUTH_REPO_REMOTE))
            )
        }
    }

    private val chatListRepoModule = module {
        single(named(NAME_CHAT_LIST_REPO_DECORATOR)) {
            getChatListRepoRemote(get())
        }
    }

    private val chatRepoModule = module {
        single(named(NAME_CHAT_REPO_DECORATOR)) {
            getChatRepoDecorator(
                getChatRepoLocal(get()),
                getChatRepoRemote(get())
            )
        }
    }

    private val viewModelsModule = module {
        viewModel { ChatListViewModel(get(named(NAME_CHAT_LIST_REPO_DECORATOR)), get()) }
        viewModel { (sessionID: String, receiverID: String) ->
            ChatViewModel(get(named(NAME_CHAT_REPO_DECORATOR)), sessionID, receiverID) }
        viewModel { AuthViewModel(get(named(NAME_AUTH_REPO_DECORATOR)), get()) }
    }


    val allModules = listOf(thingsModule, UDPAndTCPWorkersModule, chatRepoModule, chatListRepoModule, authRepoModule, viewModelsModule)
}