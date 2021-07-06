package com.edu.alterjuicechat.data.network.model.dto

data class MessageDto(val from: UserDto, val message: String) : Payload