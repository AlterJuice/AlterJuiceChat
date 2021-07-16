package com.edu.alterjuicechat.di.modules

import android.content.SharedPreferences
import com.edu.alterjuicechat.data.repo.auth.AuthRepoDecorator
import com.edu.alterjuicechat.data.repo.auth.AuthRepoLocal
import com.edu.alterjuicechat.data.repo.auth.AuthRepoRemote
import com.edu.alterjuicechat.data.repo.chat.ChatRepo
import com.edu.alterjuicechat.data.repo.chat.ChatRepoDecorator
import com.edu.alterjuicechat.data.repo.chat.ChatRepoLocal
import com.edu.alterjuicechat.data.repo.chat.ChatRepoRemote
import com.edu.alterjuicechat.data.repo.chatlist.ChatListRepoRemote
import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.*
import com.google.gson.Gson

object ModulesProvider {
    internal fun getTcpWorker(gson: Gson, dataStore: DataStore): TCPWorker { return TCPWorkerImpl(gson, dataStore) }
    internal fun getDataStore(): DataStore { return DataStoreImpl() }

    internal fun getAuthRepoLocal(profilePreferences: SharedPreferences): AuthRepo = AuthRepoLocal(profilePreferences)
    internal fun getAuthRepoRemote(tcpWorker: TCPWorker, udpWorker: UDPWorker): AuthRepo = AuthRepoRemote(tcpWorker, udpWorker)
    internal fun getAuthRepoDecorator(dataStore: DataStore, localAuthRepo: AuthRepo, remoteAuthRepo: AuthRepo): AuthRepo = AuthRepoDecorator(dataStore, localAuthRepo, remoteAuthRepo)

    internal fun getChatListRepoRemote(tcpWorker: TCPWorker): ChatListRepo = ChatListRepoRemote(tcpWorker)

    internal fun getChatRepoLocal(dataStore: DataStore): ChatRepo = ChatRepoLocal(dataStore)
    internal fun getChatRepoRemote(tcpWorker: TCPWorker): ChatRepo = ChatRepoRemote(tcpWorker)
    internal fun getChatRepoDecorator(localChatRepo: ChatRepo, remoteChatRepo: ChatRepo): ChatRepo = ChatRepoDecorator(localChatRepo, remoteChatRepo)


}