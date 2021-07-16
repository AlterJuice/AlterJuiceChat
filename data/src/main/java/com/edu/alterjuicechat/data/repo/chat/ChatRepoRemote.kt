package com.edu.alterjuicechat.data.repo.chat

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.socket.dto.entities.MessageDto

internal class ChatRepoRemote(private val tcpWorker: TCPWorker) : ChatRepo {
    override suspend fun sendMessage(
        receiverID: String,
        message: String
    ) {
        return tcpWorker.sendMessage(receiverID, message)
    }

    override fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        throw UnsupportedOperationException("Remote chat repo cannot perform getMessagesByID request")
    }

    override fun clearUnreadCounter(userID: String) {
        throw UnsupportedOperationException("Remote chat repo cannot perform clearUnreadCounter request")
    }

}