package com.edu.alterjuicechat.data.repo.chatlist

import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.TCPWorker

internal class ChatListRepoRemote(
    private val tcpWorker: TCPWorker
) : ChatListRepo {

    override suspend fun requestUsers() {
        tcpWorker.requestUsers()
    }

}