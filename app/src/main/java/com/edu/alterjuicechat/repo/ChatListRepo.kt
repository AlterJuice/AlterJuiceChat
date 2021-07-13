package com.edu.alterjuicechat.repo

interface ChatListRepo {
    suspend fun requestUsers()

}