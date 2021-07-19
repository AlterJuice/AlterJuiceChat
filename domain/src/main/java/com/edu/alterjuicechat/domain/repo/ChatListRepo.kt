package com.edu.alterjuicechat.domain.repo

import com.edu.alterjuicechat.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ChatListRepo {
    suspend fun requestUsers()
    fun getMutableUsers(): Flow<List<User>>

}