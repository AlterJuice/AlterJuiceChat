package com.edu.alterjuicechat.repo.remoteImpl

import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo

class ChatRepoRemote(private val tcpWorker: TCPWorker): ChatRepo {
    override suspend fun sendMessage(sessionID: String, receiverID: String, message: String): MessageDto {
        return tcpWorker.sendMessage(sessionID, receiverID, message)
    }
}