package com.edu.alterjuicechat.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.NetworkWorker
import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.SendMessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel  (
    // private val messagesRepoDecorator: MessagesRepo,
                      private val network: NetworkWorker
) : ViewModel() {
    private var counter = 0
    private val liveMessages: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val messages: LiveData<List<MessageDto>> = liveMessages

    init {
        getMessages()
        viewModelScope.launch {
            network.sharedFlow.collect {
                if (it.action == BaseDto.Action.SEND_MESSAGE){
                    Log.d("ChatViewModel", it.toString())
                    // with(it.payload as SendMessageDto){
                    //     liveMessages.value = (liveMessages.value ?: listOf()) +
                    //             MessageDto(UserDto("-", it.payload.receiver), it.payload.message)
                    // }
                }
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                liveMessages.value = listOf() // messagesRepoDecorator.getMessages()
            }
        }
    }
    private fun generateMessageId(): String{
        return (counter++).toString()
    }

    fun sendMessage(receiver: String, message: String){
        viewModelScope.launch(Dispatchers.IO) {

            val messageId = generateMessageId()
            val msgDto = network.sendMessage(messageId, receiver, message)
            // val msgDto = messagesRepoDecorator.sendMessage(messageId, receiver, message)
            // withContext(Dispatchers.Main) {
            //     liveMessages.value = (liveMessages.value ?: listOf()) + msgDto
            // }
        }

    }

    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            val id = generateMessageId()
            network.getUsers(id)
        }
    }
}