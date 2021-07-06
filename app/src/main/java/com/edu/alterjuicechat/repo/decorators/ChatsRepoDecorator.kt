package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.interfaces.ChatsRepo

class ChatsRepoDecorator(
    private val localChatsRepo: ChatsRepo,
    private val remoteChatsRepo: ChatsRepo
): ChatsRepo {
    override fun getChats(): List<UserDto> {
        return remoteChatsRepo.getChats()
    }
}