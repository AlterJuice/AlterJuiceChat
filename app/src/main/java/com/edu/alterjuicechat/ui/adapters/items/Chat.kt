package com.edu.alterjuicechat.ui.adapters.items

import com.edu.alterjuicechat.data.network.dto.model.UserDto

class Chat(
    val username: String,
    val userID: String,
    var lastMessage: Message,
    var countUnreadMessages: Int = 0
) {
    fun generateUserDto(): UserDto {
        return UserDto(userID, username)
    }
    fun getFormattedDate(): String{
        return lastMessage.getFormattedDate()
    }

}