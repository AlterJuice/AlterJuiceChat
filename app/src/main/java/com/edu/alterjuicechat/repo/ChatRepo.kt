package com.edu.alterjuicechat.repo

import com.edu.alterjuicechat.data.network.model.dto.MessageDto

interface ChatRepo {

    suspend fun sendMessage(sessionID: String, receiverID: String, message: String): MessageDto
    // suspend fun saveMessage(sess)


}