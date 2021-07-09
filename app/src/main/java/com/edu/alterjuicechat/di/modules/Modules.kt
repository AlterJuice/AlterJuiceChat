package com.edu.alterjuicechat.di.modules

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.repo.AuthRepo
import com.edu.alterjuicechat.repo.ChatListRepo
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.repo.decorators.AuthRepoDecorator
import com.edu.alterjuicechat.repo.decorators.ChatListRepoDecorator
import com.edu.alterjuicechat.repo.decorators.ChatRepoDecorator
import com.edu.alterjuicechat.repo.localImpl.AuthRepoLocal
import com.edu.alterjuicechat.repo.localImpl.ChatListRepoLocal
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

const val NAME_CHAT_REPO_LOCAL = "ChatRepoLocal"
const val NAME_CHAT_REPO_REMOTE = "ChatRepoRemote"
const val NAME_CHAT_REPO_DECORATOR = "ChatRepoDecorator"

const val NAME_CHAT_LIST_REPO_LOCAL = "ChatListRepoLocal"
const val NAME_CHAT_LIST_REPO_REMOTE = "ChatListRepoRemote"
const val NAME_CHAT_LIST_REPO_DECORATOR = "ChatListRepoDecorator"

object Modules {

    private val thingsModule = module{
        // get context via androidApplication() or just get() extension function
        factory { androidContext().getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }
        single { Gson() }
    }

    private val UDPAndTCPWorkersModule = module{
        single { UDPWorker(get()) }
        single { TCPWorker(get()) }
    }

    private fun getAuthRepoLocal(profilePreferences: SharedPreferences): AuthRepo = AuthRepoLocal(profilePreferences)
    private fun getAuthRepoRemote(tcpWorker: TCPWorker, udpWorker: UDPWorker): AuthRepo = AuthRepoRemote(tcpWorker, udpWorker)
    private fun getAuthRepoDecorator(localAuthRepo: AuthRepo, remoteAuthRepo: AuthRepo): AuthRepo = AuthRepoDecorator(localAuthRepo, remoteAuthRepo)

    private fun getChatListRepoLocal(): ChatListRepo = ChatListRepoLocal()
    private fun getChatListRepoRemote(tcpWorker: TCPWorker): ChatListRepo = ChatListRepoRemote(tcpWorker)
    private fun getChatListRepoDecorator(localChatListRepo: ChatListRepo, remoteChatListRepo: ChatListRepo): ChatListRepo = ChatListRepoDecorator(localChatListRepo, remoteChatListRepo)

    private fun getChatRepoLocal(): ChatRepo = ChatRepoLocal()
    private fun getChatRepoRemote(tcpWorker: TCPWorker): ChatRepo = ChatRepoRemote(tcpWorker)
    private fun getChatRepoDecorator(localChatRepo: ChatRepo, remoteChatRepo: ChatRepo): ChatRepo = ChatRepoDecorator(localChatRepo, remoteChatRepo)



    private val authRepoModule = module{
        single(named(NAME_AUTH_REPO_LOCAL)) { getAuthRepoLocal(get()) }
        single(named(NAME_AUTH_REPO_REMOTE)) { getAuthRepoRemote(get(), get()) }
        single(named(NAME_AUTH_REPO_DECORATOR)) {
            getAuthRepoDecorator(
                get(named(NAME_AUTH_REPO_LOCAL)),
                get(named(NAME_AUTH_REPO_REMOTE))
            )
        }
    }

    private val chatListRepoModule = module {
        single(named(NAME_CHAT_LIST_REPO_LOCAL)) { getChatListRepoLocal() }
        single(named(NAME_CHAT_LIST_REPO_REMOTE)) { getChatListRepoRemote(get()) }
        single(named(NAME_CHAT_LIST_REPO_DECORATOR)) {
            getChatListRepoDecorator(
                get(named(NAME_CHAT_LIST_REPO_LOCAL)),
                get(named(NAME_CHAT_LIST_REPO_REMOTE))
            ) as ChatListRepo
        }
    }

    private val chatRepoModule = module {
        single(named(NAME_CHAT_REPO_LOCAL)) { getChatRepoLocal() }
        single(named(NAME_CHAT_REPO_REMOTE)) { getChatRepoRemote(get()) }
        single(named(NAME_CHAT_REPO_DECORATOR)) {
            getChatRepoDecorator(
                get(named(NAME_CHAT_REPO_LOCAL)),
                get(named(NAME_CHAT_REPO_REMOTE))
            )
        }
    }

    private val viewModelsModule = module {
        viewModel { (sessionID: String) ->
            ChatListViewModel(get(named(NAME_CHAT_LIST_REPO_DECORATOR)), sessionID)
        }
        viewModel { (sessionID: String, receiverID: String) ->
            ChatViewModel(get(named(NAME_CHAT_REPO_DECORATOR)), get(), sessionID, receiverID) }
        viewModel { AuthViewModel(get(named(NAME_AUTH_REPO_DECORATOR))) }
    }


    val allModules = listOf(thingsModule, UDPAndTCPWorkersModule, chatRepoModule, chatListRepoModule, authRepoModule, viewModelsModule)
}