package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import com.edu.alterjuicechat.socket.dto.entities.UserDto
import kotlinx.coroutines.flow.Flow

interface DataStore  {

    fun setSessionID(newSessionID: String)
    fun setUsername(newUsername: String)
    fun setTcpIp(newTcpIp: String)

    fun getTcpIpValue(): String
    fun getSessionIDValue(): String
    fun getUsernameValue(): String

    fun getMutableTcpIp(): Flow<String>
    fun getMutableSessionID(): Flow<String>
    fun getMutableUsername(): Flow<String>

    fun getMutableUsers(): Flow<List<UserInfo>>
    fun getMutableMessages(userID: String): Flow<List<MessageDto>>

    fun handleReceiveMessage(messageDto: MessageDto)
    fun handleSendMessage(sessionID: String, chatID: String, message: String)
    fun handleNewUsers(newUsers: List<UserDto>)
    fun clearMessagesCounter(chatID: String)

}