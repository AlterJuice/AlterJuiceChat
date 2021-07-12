package com.edu.alterjuicechat.repo.localImpl

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo
import java.lang.Exception

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

    override fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        return dataStore.getMutableMessagesByID(userID)
    }
}