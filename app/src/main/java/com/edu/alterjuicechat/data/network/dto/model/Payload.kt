package com.edu.alterjuicechat.data.network.dto.model

import com.google.gson.Gson

interface Payload

fun Payload.toJson(gson: Gson): String{
    return gson.toJson(this)
}