package com.edu.alterjuicechat.socket.dto.entities

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload