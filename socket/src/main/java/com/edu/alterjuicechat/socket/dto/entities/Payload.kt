package com.edu.alterjuicechat.socket.dto.entities

import com.google.gson.Gson

interface Payload

fun Payload.toJson(gson: Gson): String{
    return gson.toJson(this)
}