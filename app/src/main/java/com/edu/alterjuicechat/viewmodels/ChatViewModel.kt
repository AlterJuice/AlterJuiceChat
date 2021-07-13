package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.repo.ChatRepo
import com.edu.alterjuicechat.ui.adapters.items.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel  (
    private val chatRepoDecorator: ChatRepo,
    private val sessionID: String,
    private val receiverID: String
) : ViewModel() {
    val messages: LiveData<List<Message>> = chatRepoDecorator.getMessagesByID(receiverID)

    fun sendMessage(messageText: String){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepoDecorator.sendMessage(sessionID, receiverID, messageText)
        }
    }

    fun clearUnreadCounter(){
        chatRepoDecorator.clearUnreadCounter(receiverID)
    }
}