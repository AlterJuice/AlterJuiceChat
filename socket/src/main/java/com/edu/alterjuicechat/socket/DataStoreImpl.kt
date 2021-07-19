package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import com.edu.alterjuicechat.socket.dto.entities.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import kotlin.collections.HashMap

internal class DataStoreImpl : DataStore {
    private val _mutableTcpIP = MutableStateFlow("")
    private val _mutableSessionID = MutableStateFlow("")
    private val _mutableUsername = MutableStateFlow("")

    private val messages: HashMap<String, MutableStateFlow<List<MessageDto>>> = HashMap()

    private val _mutableUsersInfo = MutableStateFlow<List<UserInfo>>(listOf())
    private val usersInfo = HashMap<String, UserInfo>()

    private fun createMessageFieldIfNotExists(userID: String) {
        if (!messages.containsKey(userID)) {
            messages[userID] = MutableStateFlow(listOf())
        }
    }

    override fun getMutableTcpIp() = _mutableTcpIP
    override fun getMutableSessionID() = _mutableSessionID
    override fun getMutableUsername() = _mutableUsername
    override fun getMutableUsers() = _mutableUsersInfo

    override fun getMutableMessages(userID: String): MutableStateFlow<List<MessageDto>> {
        createMessageFieldIfNotExists(userID)
        return messages[userID]!!
    }

    private fun updateMutableUsers(){
        // update users after changes
        _mutableUsersInfo.value = usersInfo.values.sortedWith(compareBy { it.lastMessageDateMilliseconds })
    }


    private fun addMessage(userInfo: UserInfo, message: MessageDto){
        val msgList = getMutableMessages(userInfo.chatID).value
        messages[userInfo.chatID]?.value = msgList + message
    }

    private fun updateLastMessage(userInfo: UserInfo, message: String, dateMilliseconds: Long) {
        userInfo.lastMessage = message
        userInfo.lastMessageDateMilliseconds = dateMilliseconds
    }

    private fun getOrCreateUserInfo(chatID: String, name: String): UserInfo{
        if (!usersInfo.containsKey(chatID))
            setUserInfo(UserInfo(chatID, name))
        return usersInfo[chatID]!!
    }

    private fun handleMessage(chatID: String, messageDto: MessageDto, increaseCounter: Boolean){
        val userInfo = getOrCreateUserInfo(chatID, messageDto.from.name)
        if (increaseCounter)
            userInfo.unreadMessagesCount += 1
        addMessage(userInfo, messageDto)
        updateLastMessage(userInfo, messageDto.message, getCurrentTime())
        setUserInfo(userInfo)
        updateMutableUsers()
    }

    override fun handleReceiveMessage(messageDto: MessageDto) {
        // called from Main Context
        val chatID = messageDto.from.id
        handleMessage(chatID, messageDto, true)
    }


    override fun handleSendMessage(sessionID: String, chatID: String, message: String) {
        // called from Main Context
        val messageDto = MessageDto(from=UserDto(sessionID, _mutableUsername.value), message)
        handleMessage(chatID, messageDto, false)
    }

    override fun handleNewUsers(newUsers: List<UserDto>) {
        newUsers.forEach {
            if (!usersInfo.containsKey(it.id))
                setUserInfo(UserInfo(it.id, it.name))
        }
        updateMutableUsers()
    }

    override fun clearMessagesCounter(chatID: String) {
        usersInfo[chatID]?.unreadMessagesCount = 0
        updateMutableUsers()
    }

    private fun setUserInfo(userInfo: UserInfo){
        usersInfo[userInfo.chatID] = userInfo
    }

    private fun getCurrentTime(): Long{
        return Date().time
    }
}