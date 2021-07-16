package com.edu.alterjuicechat.data.repo.chat

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.socket.dto.entities.MessageDto

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

    override fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        return localChatRepoDecorator.getMessagesByID(userID)
    }

    override fun clearUnreadCounter(userID: String) {
        localChatRepoDecorator.clearUnreadCounter(userID)
    }


}