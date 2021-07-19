package com.edu.alterjuicechat.data.repo.chatlist

import com.edu.alterjuicechat.domain.model.User
import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.socket.UserInfo
import com.edu.alterjuicechat.socket.dto.entities.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ChatListRepoRemote(
    private val tcpWorker: TCPWorker,
    private val dataStore: DataStore
) : ChatListRepo {

    override suspend fun requestUsers() {
        tcpWorker.requestUsers()
    }

    override fun getMutableUsers(): Flow<List<User>> {
        return dataStore.getMutableUsers().map { it.map { it.toDomain() } }
    }
}

fun UserInfo.toDomain() = User(chatID, chatName, lastMessage, lastMessageDateMilliseconds, unreadMessagesCount)