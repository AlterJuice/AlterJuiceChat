package com.edu.alterjuicechat.domain.repo

interface ChatListRepo {
    suspend fun requestUsers()

}