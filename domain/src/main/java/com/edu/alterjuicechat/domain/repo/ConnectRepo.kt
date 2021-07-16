package com.edu.alterjuicechat.domain.repo

interface ConnectRepo {
    suspend fun connect()
    suspend fun disconnect(code: Int)
    suspend fun requestSessionID()
    suspend fun requestTcpIP()
}
