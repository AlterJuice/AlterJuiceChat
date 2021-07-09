package com.edu.alterjuicechat.repo.localImpl

import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.ChatListRepo
import java.lang.UnsupportedOperationException

class ChatListRepoLocal: ChatListRepo {
    override suspend fun getUsers(sessionID: String): List<UserDto> {
        throw UnsupportedOperationException("Local chat repo cannot perform getUsers request")
    }
}