package com.edu.alterjuicechat.repo.remoteImpl

import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.ChatListRepo

class ChatListRepoRemote(
    private val tcpWorker: TCPWorker
): ChatListRepo {
    override suspend fun getUsers(sessionID: String): List<UserDto> {
        return tcpWorker.getUsers(sessionID).users
    }
}