package com.edu.alterjuicechat.socket.dto.entities

data class MessageDto(val from: UserDto, val message: String) : Payload