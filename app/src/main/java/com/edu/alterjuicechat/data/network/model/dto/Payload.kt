package com.edu.alterjuicechat.data.network.model.dto

import com.google.gson.Gson

interface Payload

fun Payload.toJson(gson: Gson): String{
    return gson.toJson(this)
}