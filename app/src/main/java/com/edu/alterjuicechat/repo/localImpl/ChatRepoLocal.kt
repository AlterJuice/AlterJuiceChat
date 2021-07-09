package com.edu.alterjuicechat.repo.localImpl

import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo

class ChatRepoLocal: ChatRepo {
    override suspend fun sendMessage(sessionID: String, receiverID: String, message: String): MessageDto {
        throw UnsupportedOperationException("Local chat repo cannot perform sendMessage request")
    }

}