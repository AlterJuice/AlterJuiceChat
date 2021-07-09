package com.edu.alterjuicechat.repo

import com.edu.alterjuicechat.data.network.model.dto.UserDto

interface ChatListRepo {
    suspend fun getUsers(sessionID: String): List<UserDto>

}