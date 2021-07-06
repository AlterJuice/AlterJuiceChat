package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.data.network.model.dto.*
import com.google.gson.Gson

class GeneratorDto(private val gson: Gson) {


    fun generatePing(pingId: String): BaseDto{
        return generateAction(BaseDto.Action.PING, PingDto(pingId))
    }
    fun generateSendMessage(messageId: String, receiver: String, message: String): BaseDto{
        return generateAction(BaseDto.Action.SEND_MESSAGE, SendMessageDto(messageId, receiver, message))
    }

    fun generateConnect(connectId: String, connectName: String): BaseDto{
        return generateAction(BaseDto.Action.CONNECT, ConnectDto(connectId, connectName))
    }

    fun generateGetUsers(getUsersId: String): BaseDto{
        return generateAction(BaseDto.Action.GET_USERS, GetUsersDto(getUsersId))
    }

    private fun generateAction(action: BaseDto.Action, payload: Payload): BaseDto {
        return BaseDto(action, objectToJson(payload))
    }

    fun objectToJson(obj: Any): String {
        return gson.toJson(obj)
    }
}