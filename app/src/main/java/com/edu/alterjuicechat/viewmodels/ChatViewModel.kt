package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.repo.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel  (
    private val chatRepoDecorator: ChatRepo,
    private val sessionID: String,
    private val receiverID: String
) : ViewModel() {
    private val liveMessages: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val messages: LiveData<List<MessageDto>> = liveMessages


    fun sendMessage(messageText: String){
        viewModelScope.launch(Dispatchers.IO) {
            val messageDto = chatRepoDecorator.sendMessage(sessionID, receiverID, messageText)
            withContext(Dispatchers.Main){
                liveMessages.value = (liveMessages.value?: listOf()) + messageDto
            }
        }
    }

}