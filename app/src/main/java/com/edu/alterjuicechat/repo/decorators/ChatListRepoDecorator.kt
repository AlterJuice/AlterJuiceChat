package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.repo.ChatListRepo

class ChatListRepoDecorator(
    private val remoteChatListRepo: ChatListRepo
) : ChatListRepo {

    override suspend fun requestUsers() {
        remoteChatListRepo.requestUsers()
    }



}