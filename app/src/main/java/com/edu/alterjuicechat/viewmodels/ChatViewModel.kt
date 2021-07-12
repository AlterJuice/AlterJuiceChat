package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel  (
    private val chatRepoDecorator: ChatRepo,
    private val sessionID: String,
    private val receiverID: String
) : ViewModel() {
    val messages: LiveData<List<MessageDto>> = chatRepoDecorator.getMessagesByID(receiverID)

    fun sendMessage(messageText: String){
        viewModelScope.launch(Dispatchers.IO) {
            chatRepoDecorator.sendMessage(sessionID, receiverID, messageText)
        }
    }
}