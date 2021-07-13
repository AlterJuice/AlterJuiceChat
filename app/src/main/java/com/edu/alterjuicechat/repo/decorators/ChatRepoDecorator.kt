package com.edu.alterjuicechat.repo.decorators

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.ui.adapters.items.Message

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

    override fun getMessagesByID(userID: String): MutableLiveData<List<Message>> {
        return localChatRepoDecorator.getMessagesByID(userID)
    }

    override fun clearUnreadCounter(userID: String) {
        localChatRepoDecorator.clearUnreadCounter(userID)
    }


}