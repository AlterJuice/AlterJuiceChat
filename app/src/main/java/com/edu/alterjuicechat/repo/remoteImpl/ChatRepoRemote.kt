package com.edu.alterjuicechat.repo.remoteImpl

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.ui.adapters.items.Message
import java.lang.UnsupportedOperationException

class ChatRepoRemote(private val tcpWorker: TCPWorker) : ChatRepo {
    override suspend fun sendMessage(
        sessionID: String,
        receiverID: String,
        message: String
    ) {
        return tcpWorker.sendMessage(sessionID, receiverID, message)
    }

    override fun getMessagesByID(userID: String): MutableLiveData<List<Message>> {
        throw UnsupportedOperationException("Remote chat repo cannot perform getMessagesByID request")
    }

    override fun clearUnreadCounter(userID: String) {
        throw UnsupportedOperationException("Remote chat repo cannot perform clearUnreadCounter request")
    }

}