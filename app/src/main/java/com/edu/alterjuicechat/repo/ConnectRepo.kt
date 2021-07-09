package com.edu.alterjuicechat.repo

interface ConnectRepo {
    suspend fun connect(sessionID: String, username: String)
    suspend fun disconnect(sessionID: String, code: Int)
    suspend fun getSessionID(tcpIP: String, username: String): String
    suspend fun getTcpIP(): String
}
