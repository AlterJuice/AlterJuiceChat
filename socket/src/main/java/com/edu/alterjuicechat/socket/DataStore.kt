package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import com.edu.alterjuicechat.socket.dto.entities.UserDto
import kotlinx.coroutines.flow.MutableStateFlow

interface DataStore  {

    fun getMutableTcpIp(): MutableStateFlow<String>
    fun getMutableSessionID(): MutableStateFlow<String>
    fun getMutableUsername(): MutableStateFlow<String>

    fun getMutableUsers(): MutableStateFlow<List<UserInfo>>
    fun getMutableMessages(userID: String): MutableStateFlow<List<MessageDto>>

    fun handleReceiveMessage(messageDto: MessageDto)
    fun handleSendMessage(sessionID: String, chatID: String, message: String)
    fun handleNewUsers(newUsers: List<UserDto>)
    fun clearMessagesCounter(chatID: String)

}