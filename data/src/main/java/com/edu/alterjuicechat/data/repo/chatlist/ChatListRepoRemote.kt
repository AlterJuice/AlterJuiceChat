package com.edu.alterjuicechat.data.repo.chatlist

import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.socket.UserInfo
import com.edu.alterjuicechat.socket.dto.entities.UserDto
import kotlinx.coroutines.flow.Flow

internal class ChatListRepoRemote(
    private val tcpWorker: TCPWorker,
    private val dataStore: DataStore
) : ChatListRepo {

    override suspend fun requestUsers() {
        tcpWorker.requestUsers()
    }

    override fun getMutableUsers(): Flow<List<UserInfo>> {
        return dataStore.getMutableUsers()
    }
}