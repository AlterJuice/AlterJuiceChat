package com.edu.alterjuicechat.repo.localImpl

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.repo.DataStore
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.ui.adapters.items.Message

class ChatRepoLocal(
    private val dataStore: DataStore
) : ChatRepo {
    override suspend fun sendMessage(
        sessionID: String,
        receiverID: String,
        message: String
    ) {
        throw Exception()
    }

    override fun getMessagesByID(userID: String): MutableLiveData<List<Message>> {
        return dataStore.getMutableMessagesByID(userID)
    }

    override fun clearUnreadCounter(userID: String) {
        dataStore.clearUnreadCounter(userID)
    }
}