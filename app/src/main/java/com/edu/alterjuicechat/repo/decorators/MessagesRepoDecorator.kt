package com.edu.alterjuicechat.repo.decorators

import android.util.Log
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.SendMessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo

class MessagesRepoDecorator
    (private val localMessagesRepo: MessagesRepo,
     private val remoteMessagesRepo: MessagesRepo

            ): MessagesRepo {
    override fun getMessages(offset: Int): List<MessageDto> {
        return listOf()
    }

    override fun saveMessage(id: String, text: String) {
        Log.d("MessagesRepoDecorator", "Msg saved")
    }

    override fun sendMessage(messageId: String, receiver: String, message: String): MessageDto {
        // return SendMessageDto(messageId, receiver, message) as MessageDto
        TODO("error")
        return MessageDto(UserDto("-", "-"), "-")
    }


}