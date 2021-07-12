package com.edu.alterjuicechat.repo.remoteImpl

import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.repo.ChatListRepo

class ChatListRepoRemote(
    private val tcpWorker: TCPWorker
) : ChatListRepo {


    override suspend fun requestUsers() {
        tcpWorker.requestUsers()
    }

}