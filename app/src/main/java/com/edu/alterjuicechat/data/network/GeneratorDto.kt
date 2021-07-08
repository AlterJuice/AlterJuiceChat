package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.data.network.model.dto.*
import com.google.gson.Gson

class GeneratorDto(private val gson: Gson) {

    val Payload.toJson: String
        get() = gson.toJson(this)

    /* ID is SessionID got from first TCP socket connection. */

    fun generatePing(id: String): BaseDto{
        return generateAction(BaseDto.Action.PING, PingDto(id))
    }
    fun generateSendMessage(id: String, receiver: String, message: String): BaseDto{
        return generateAction(BaseDto.Action.SEND_MESSAGE, SendMessageDto(id, receiver, message))
    }

    fun generateConnect(id: String, connectName: String): BaseDto{
        return generateAction(BaseDto.Action.CONNECT, ConnectDto(id, connectName))
    }

    fun generateGetUsers(id: String): BaseDto{
        return generateAction(BaseDto.Action.GET_USERS, GetUsersDto(id))
    }

    private fun generateAction(action: BaseDto.Action, payload: Payload): BaseDto {
        return BaseDto(action, payload.toJson)
    }
}