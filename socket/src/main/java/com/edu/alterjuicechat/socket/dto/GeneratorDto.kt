package com.edu.alterjuicechat.socket.dto

import com.edu.alterjuicechat.socket.dto.entities.*
import com.google.gson.Gson

class GeneratorDto(private val gson: Gson) {

    /* ID is SessionID got from first TCP socket connection. */

    fun generatePing(id: String): BaseDto {
        return generateAction(BaseDto.Action.PING, PingDto(id))
    }
    fun generateSendMessage(id: String, receiver: String, message: String): BaseDto {
        return generateAction(BaseDto.Action.SEND_MESSAGE, SendMessageDto(id, receiver, message))
    }

    fun generateConnect(id: String, connectName: String): BaseDto {
        return generateAction(BaseDto.Action.CONNECT, ConnectDto(id, connectName))
    }

    fun generateDisconnect(id: String, code: Int): BaseDto {
        return generateAction(BaseDto.Action.DISCONNECT, DisconnectDto(id, code))
    }

    fun generateGetUsers(id: String): BaseDto {
        return generateAction(BaseDto.Action.GET_USERS, GetUsersDto(id))
    }

    private fun generateAction(action: BaseDto.Action, payload: Payload): BaseDto {
        return BaseDto(action, payload.toJson(gson))
    }
}