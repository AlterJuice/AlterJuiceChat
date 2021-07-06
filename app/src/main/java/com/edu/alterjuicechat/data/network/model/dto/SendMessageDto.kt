package com.edu.alterjuicechat.data.network.model.dto

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload