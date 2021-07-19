package com.edu.alterjuicechat.data.repo

import android.content.SharedPreferences
import com.edu.alterjuicechat.data.repo.auth.AuthRepoDecorator
import com.edu.alterjuicechat.data.repo.auth.AuthRepoLocal
import com.edu.alterjuicechat.data.repo.auth.AuthRepoRemote
import com.edu.alterjuicechat.domain.repo.ChatRepo
import com.edu.alterjuicechat.data.repo.chat.ChatRepoDecorator
import com.edu.alterjuicechat.data.repo.chat.ChatRepoLocal
import com.edu.alterjuicechat.data.repo.chat.ChatRepoRemote
import com.edu.alterjuicechat.data.repo.chatlist.ChatListRepoRemote
import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.socket.UDPWorker
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val NAME_AUTH_REPO_DECORATOR = "AuthRepoDecorator"
const val NAME_CHAT_REPO_DECORATOR = "ChatRepoDecorator"
const val NAME_CHAT_LIST_REPO_DECORATOR = "ChatListRepoDecorator"

object DI {
    private fun getAuthRepoLocal(dataStore: DataStore, profilePreferences: SharedPreferences): AuthRepo = AuthRepoLocal(dataStore, profilePreferences)
    private fun getAuthRepoRemote(tcpWorker: TCPWorker, udpWorker: UDPWorker): AuthRepo = AuthRepoRemote(tcpWorker, udpWorker)
    private fun getAuthRepoDecorator(localAuthRepo: AuthRepo, remoteAuthRepo: AuthRepo): AuthRepo = AuthRepoDecorator(localAuthRepo, remoteAuthRepo)

    private fun getChatListRepoRemote(tcpWorker: TCPWorker, dataStore: DataStore): ChatListRepo = ChatListRepoRemote(tcpWorker, dataStore)

    private fun getChatRepoLocal(dataStore: DataStore): ChatRepo = ChatRepoLocal(dataStore)
    private fun getChatRepoRemote(tcpWorker: TCPWorker): ChatRepo = ChatRepoRemote(tcpWorker)
    private fun getChatRepoDecorator(localChatRepo: ChatRepo, remoteChatRepo: ChatRepo): ChatRepo = ChatRepoDecorator(localChatRepo, remoteChatRepo)

    val modules = module {
        single(named(NAME_AUTH_REPO_DECORATOR)) {
            getAuthRepoDecorator(
                getAuthRepoLocal(get(), get()),
                getAuthRepoRemote(get(), get())
            )
        }

        single(named(NAME_CHAT_LIST_REPO_DECORATOR)) {
            getChatListRepoRemote(get(), get())
        }

        single(named(NAME_CHAT_REPO_DECORATOR)) {
            getChatRepoDecorator(
                getChatRepoLocal(get()),
                getChatRepoRemote(get())
            )
        }
    }
}