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
    private val tcpWorker: TCPWorker,
    private val sessionID: String,
    private val receiverID: String
) : ViewModel() {
    private val liveMessages: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val messages: LiveData<List<MessageDto>> = liveMessages

    init {

        tcpWorker.launchEventFlows()
        viewModelScope.launch(Dispatchers.IO){
            tcpWorker.newMessagesFlow.collect(){
                withContext(Dispatchers.Main){
                    processNewMessage(it)
                }
            }
        }
    }

    fun sendMessage(messageText: String){
        viewModelScope.launch(Dispatchers.IO) {
            val messageDto = chatRepoDecorator.sendMessage(sessionID, receiverID, messageText)
            withContext(Dispatchers.Main){
                processNewMessage(messageDto)
            }
        }
    }
    private fun processNewMessage(message: MessageDto){
        liveMessages.value = (liveMessages.value?: listOf()) + message

    }
}