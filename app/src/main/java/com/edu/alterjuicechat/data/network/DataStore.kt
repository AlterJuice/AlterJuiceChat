package com.edu.alterjuicechat.data.network

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.model.Chat
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto

class DataStore {
    val mutableTcpIP = MutableLiveData<String>()
    val mutableSessionID = MutableLiveData<String>()
    val mutableUsername = MutableLiveData<String>()
    val mutableUsers = MutableLiveData<List<Chat>>()
    private val lastMessages = HashMap<String ,String>()

    private val messages: HashMap<String, MutableLiveData<List<MessageDto>>> = HashMap()


    fun processRawUsers(newUsers: List<UserDto>){
        val formattedChats = ArrayList<Chat>()
        newUsers.forEach {
            formattedChats.add(Chat(it.name, it.id, getLastMessageForByUserID(it.id)))
        }
        this.mutableUsers.postValue(formattedChats)
    }

    fun processNewMessage(message: MessageDto){
        val msgList = getMutableMessagesByID(message.from.id).value
        messages[message.from.id]!!.value = ((msgList?: listOf()) + message)
        updateLastMessage(message.from.id, message.message)
    }

    fun processSendMessage(sessionID: String, receiverID: String, message: String){
        val msgList = getMutableMessagesByID(receiverID).value
        messages[receiverID]!!.postValue((msgList?: listOf()) + MessageDto(UserDto(sessionID, "ME"), message))
        updateLastMessage(receiverID, message)
    }
    fun updateLastMessage(chatID: String, messageText: String){
        lastMessages[chatID] = messageText
        processUsers(mutableUsers.value!!)
    }

    fun getMutableMessagesByID(userID: String): MutableLiveData<List<MessageDto>> {
        createMessageFieldIfNotExists(userID)
        return messages[userID]!!
    }

    private fun createMessageFieldIfNotExists(userID: String){
        if (!messages.containsKey(userID)){
            messages[userID] = MutableLiveData(listOf())
        }
    }
    private fun processUsers(newUsers: List<Chat>){
        newUsers.forEach {
            it.lastMessage = getLastMessageForByUserID(it.userID)
        }
        mutableUsers.postValue(newUsers)
    }

    private fun getLastMessageForByUserID(userID: String): String{
        return lastMessages.getOrDefault(userID, "History is empty")
    }

}