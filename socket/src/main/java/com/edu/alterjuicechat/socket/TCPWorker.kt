package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.entities.BaseDto

interface TCPWorker {

    suspend fun requestSessionID()
    suspend fun requestUsers()
    suspend fun connect()
    suspend fun disconnect(code: Int)
    suspend fun sendToServer(action: BaseDto)
    suspend fun sendMessage(chatID: String, message: String)
    fun convertUpdateToBaseDto(stringUpdate: String): BaseDto
    suspend fun handleUpdate(baseDto: BaseDto)
    suspend fun stopAll()

}