package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.domain.repo.ChatRepo
import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatViewModel  (
    private val chatRepoDecorator: ChatRepo,
    private val receiverID: String,
) : ViewModel() {
    val messages: Flow<List<MessageDto>> = chatRepoDecorator.getMessagesByID(receiverID)

    fun sendMessage(messageText: String){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepoDecorator.sendMessage(receiverID, messageText)
        }
    }

    fun clearUnreadCounter(){
        chatRepoDecorator.clearUnreadCounter(receiverID)
    }
}