package com.edu.alterjuicechat.domain.model

data class User(
    val chatID: String,
    val chatName: String,
    var lastMessage: String = "",
    var lastMessageDateMilliseconds: Long = 0,
    var unreadMessagesCount: Int = 0
)
