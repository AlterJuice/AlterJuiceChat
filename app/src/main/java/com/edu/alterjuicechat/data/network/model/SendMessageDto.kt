package com.edu.alterjuicechat.data.network.model

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload