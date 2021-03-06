package com.edu.alterjuicechat.data.repo.chat

import com.edu.alterjuicechat.domain.model.Message
import com.edu.alterjuicechat.domain.repo.ChatRepo
import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import kotlinx.coroutines.flow.Flow

internal class ChatRepoDecorator
    (
    private val localChatRepoDecorator: ChatRepo,
    private val remoteChatRepo: ChatRepo
) : ChatRepo {
    override suspend fun sendMessage(
        receiverID: String,
        message: String
    ) {
        return remoteChatRepo.sendMessage(receiverID, message)
    }

    override fun getMessagesByID(userID: String): Flow<List<Message>> {
        return localChatRepoDecorator.getMessagesByID(userID)
    }

    override fun clearUnreadCounter(userID: String) {
        localChatRepoDecorator.clearUnreadCounter(userID)
    }


}