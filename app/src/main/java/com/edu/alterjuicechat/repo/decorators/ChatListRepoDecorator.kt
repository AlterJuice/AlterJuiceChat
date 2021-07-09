package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.ChatListRepo

class ChatListRepoDecorator(
    private val localChatListRepo: ChatListRepo,
    private val remoteChatListRepo: ChatListRepo
): ChatListRepo {
    override suspend fun getUsers(sessionID: String): List<UserDto> {
        return remoteChatListRepo.getUsers(sessionID)
    }
}