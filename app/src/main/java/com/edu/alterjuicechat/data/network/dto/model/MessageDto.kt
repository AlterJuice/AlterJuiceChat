package com.edu.alterjuicechat.data.network.dto.model

data class MessageDto(val from: UserDto, val message: String) : Payload