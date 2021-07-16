package com.edu.alterjuicechat.socket

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import com.edu.alterjuicechat.socket.dto.entities.UserDto

interface DataStore  {

    fun getMutableTcpIp(): MutableLiveData<String>
    fun getMutableSessionID(): MutableLiveData<String>
    fun getMutableUsername(): MutableLiveData<String>

    fun getMutableUsers(): MutableLiveData<List<UserInfo>>
    fun getMutableMessages(userID: String): MutableLiveData<List<MessageDto>>

    fun handleReceiveMessage(messageDto: MessageDto)
    fun handleSendMessage(sessionID: String, chatID: String, message: String)
    fun handleNewUsers(newUsers: List<UserDto>)
    fun clearMessagesCounter(chatID: String)

}