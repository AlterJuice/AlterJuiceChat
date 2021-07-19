package com.edu.alterjuicechat.data.repo.chat

import com.edu.alterjuicechat.domain.model.Message
import com.edu.alterjuicechat.domain.repo.ChatRepo
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChatRepoLocal(
    private val dataStore: DataStore
) : ChatRepo {
    override suspend fun sendMessage(
        receiverID: String,
        message: String
    ) {
        throw UnsupportedOperationException("Local Chat repo cannot perform sendMessage request")
    }

    override fun getMessagesByID(userID: String): Flow<List<Message>> {
        return dataStore.getMutableMessages(userID).map { it.map { it.toDomain() } }
    }

    override fun clearUnreadCounter(userID: String) {
        dataStore.clearMessagesCounter(userID)
    }
}

fun MessageDto.toDomain() = Message(
    this.from.id, this.from.name, this.message
)