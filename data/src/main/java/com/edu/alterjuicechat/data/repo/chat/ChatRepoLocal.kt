package com.edu.alterjuicechat.data.repo.chat

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.dto.entities.MessageDto

internal class ChatRepoLocal(
    private val dataStore: DataStore
) : ChatRepo {
    override suspend fun sendMessage(
        receiverID: String,
        message: String
    ) {
        throw UnsupportedOperationException("Local Chat repo cannot perform sendMessage request")
    }

    override fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        return dataStore.getMutableMessages(userID)
    }

    override fun clearUnreadCounter(userID: String) {
        dataStore.clearMessagesCounter(userID)
    }
}