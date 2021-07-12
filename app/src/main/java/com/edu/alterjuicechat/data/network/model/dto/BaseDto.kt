package com.edu.alterjuicechat.data.network.model.dto

import com.google.gson.Gson

data class BaseDto(val action: Action, val payload: String) {

    enum class Action {
        PING, PONG, CONNECT, CONNECTED, GET_USERS, USERS_RECEIVED, SEND_MESSAGE, NEW_MESSAGE, DISCONNECT
    }

    fun toJson(gson: Gson): String {
        return gson.toJson(this)
    }
}