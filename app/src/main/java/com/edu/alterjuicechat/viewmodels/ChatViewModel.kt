package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel  (
    private val tcpWorker: TCPWorker
) : ViewModel() {
    private val liveMessages: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val messages: LiveData<List<MessageDto>> = liveMessages


    fun sendMessage(sessionID: String, receiver: UserDto, messageText: String){
        viewModelScope.launch {
            flow<MessageDto> {
                emit(tcpWorker.sendMessage(sessionID, receiver, messageText))
            }.flowOn(Dispatchers.IO).collect {
                withContext(Dispatchers.Main){
                    liveMessages.value = (liveMessages.value?: listOf()) + it
                }
            }
        }
    }

}