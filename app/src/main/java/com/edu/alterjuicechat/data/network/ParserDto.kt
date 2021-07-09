package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.data.network.model.dto.*
import com.google.gson.Gson

class ParserDto(private val gson: Gson){
    fun parseMessage(baseDtoMessagePayload: String): MessageDto{
        return gson.fromJson(baseDtoMessagePayload, MessageDto::class.java)
    }

    fun parseUsersReceived(baseDtoUsersReceivedPayload: String): UsersReceivedDto{
        return gson.fromJson(baseDtoUsersReceivedPayload, UsersReceivedDto::class.java)
    }
    fun parseUsersList(baseDtoUsersListPayload: String): List<UserDto>{
        return gson.fromJson(baseDtoUsersListPayload, UsersReceivedDto::class.java).users
    }
    fun parseConnected(baseDtoConnectedPayload: String): ConnectedDto{
        return gson.fromJson(baseDtoConnectedPayload, ConnectedDto::class.java)

    }
}