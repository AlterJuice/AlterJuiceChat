package com.edu.alterjuicechat.data.network

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.ui.adapters.items.Chat
import com.edu.alterjuicechat.ui.adapters.items.Message
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DataStore {
    val mutableTcpIP = MutableLiveData<String>()
    val mutableSessionID = MutableLiveData<String>()
    val mutableUsername = MutableLiveData<String>()
    val mutableUsers = MutableLiveData<List<Chat>>()
    private val lastMessages = HashMap<String, Message>()
    private val countUnreadMessages = HashMap<String, Int>()

    private val messages: HashMap<String, MutableLiveData<List<Message>>> = HashMap()


    fun processRawUsers(newUsers: List<UserDto>) {
        val formattedChats = ArrayList<Chat>()
        newUsers.forEach {
            val lastMessage = getLastMessageByUserID(it.id)
            formattedChats.add(
                Chat(
                    it.name,
                    it.id,
                    lastMessage,
                    getCountUnreadMessagesByUserID(it.id)
                )
            )
        }
        this.mutableUsers.postValue(formattedChats)
    }

    fun clearUnreadCounter(userID: String) {
        countUnreadMessages[userID] = 0
    }

    fun processNewMessage(messageDto: MessageDto) {
        val msgList = getMutableMessagesByID(messageDto.from.id).value
        val message = Message(messageDto.from.id, messageDto.message, Date())
        messages[messageDto.from.id]!!.value = ((msgList ?: listOf()) + message)
        countUnreadMessages[messageDto.from.id] = (countUnreadMessages[messageDto.from.id] ?: 0) + 1
        updateLastMessage(messageDto.from.id, message)
    }

    fun processSendMessage(sessionID: String, receiverID: String, message: String) {
        val msgList = getMutableMessagesByID(receiverID).value
        val newMsg = Message(sessionID, message, Date())
        messages[receiverID]!!.postValue((msgList ?: listOf()) + newMsg)
        updateLastMessage(receiverID, newMsg)
    }

    private fun updateLastMessage(chatID: String, message: Message) {
        lastMessages[chatID] = message
        processUsers(mutableUsers.value!!)
    }

    fun getMutableMessagesByID(userID: String): MutableLiveData<List<Message>> {
        createMessageFieldIfNotExists(userID)
        return messages[userID]!!
    }

    private fun createMessageFieldIfNotExists(userID: String) {
        if (!messages.containsKey(userID)) {
            messages[userID] = MutableLiveData(listOf())
        }
    }

    private fun processUsers(newUsers: List<Chat>) {
        newUsers.forEach {
            it.lastMessage = getLastMessageByUserID(it.userID)
            it.countUnreadMessages = getCountUnreadMessagesByUserID(it.userID)
        }
        mutableUsers.postValue(newUsers)
    }

    private fun getLastMessageByUserID(userID: String): Message {
        return lastMessages.getOrDefault(userID, Message(userID))
    }

    private fun getCountUnreadMessagesByUserID(userID: String): Int {
        return countUnreadMessages.getOrDefault(userID, 0)

    }

}