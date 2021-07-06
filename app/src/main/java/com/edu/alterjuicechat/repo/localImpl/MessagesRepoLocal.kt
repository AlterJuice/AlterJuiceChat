package com.edu.alterjuicechat.repo.localImpl

import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo

class MessagesRepoLocal: MessagesRepo {
    override fun getMessages(offset: Int): List<MessageDto> {
        TODO("Not yet implemented")
    }

    override fun saveMessage(id: String, text: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(messageId: String, receiver: String, message: String): MessageDto {
        TODO("Not yet implemented")
    }

}