package com.edu.alterjuicechat.data.network.model

import com.edu.alterjuicechat.data.network.model.dto.UserDto

class Chat(val username: String, val userID: String, var lastMessage: String = "History is empty"){
    fun generateUserDto(): UserDto {
        return UserDto(userID, username)
    }
}