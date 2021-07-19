package com.edu.alterjuicechat.socket

class UserInfo constructor(
    val chatID: String,
    val chatName: String,
    var lastMessage: String = "",
    var lastMessageDateMilliseconds: Long = 0,
    var unreadMessagesCount: Int = 0
){
    override fun toString(): String {
        return "Name: $chatName; ID: $chatID; lastMessage: $lastMessage"
    }
}