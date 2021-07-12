package com.edu.alterjuicechat.repo.decorators

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo

class ChatRepoDecorator
    (
    private val localChatRepoDecorator: ChatRepo,
    private val remoteChatRepo: ChatRepo
) : ChatRepo {
    override suspend fun sendMessage(
        sessionID: String,
        receiverID: String,
        message: String
    ) {
        return remoteChatRepo.sendMessage(sessionID, receiverID, message)
    }

    override fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        return localChatRepoDecorator.getMessagesByID(userID)
    }


}