package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo

class ChatRepoDecorator
    (private val localChatRepo: ChatRepo,
     private val remoteChatRepo: ChatRepo): ChatRepo {
    override suspend fun sendMessage(sessionID: String, receiverID: String, message: String): MessageDto {
        return remoteChatRepo.sendMessage(sessionID, receiverID, message)
    }


}