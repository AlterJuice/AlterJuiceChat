package com.edu.alterjuicechat.data.repo.auth

import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.socket.UDPWorker
import kotlinx.coroutines.flow.Flow

internal class AuthRepoRemote(
    private val tcpWorker: TCPWorker,
    private val udpWorker: UDPWorker
) : AuthRepo {

    override fun saveUsername(username: String) {
        throw UnsupportedOperationException("Remote auth repo cannot perform saveUsername request")
    }

    override fun getSavedUsername(): String {
        throw UnsupportedOperationException("Remote auth repo cannot perform getUsername request")
    }

    override fun getMutableUsername(): Flow<String> {
        throw UnsupportedOperationException("Remote auth repo cannot perform getMutableUsername request")
    }

    override fun getMutableTcpIP(): Flow<String> {
        throw UnsupportedOperationException("Remote auth repo cannot perform getMutableTcpIP request")
    }

    override fun getMutableSessionID(): Flow<String> {
        throw UnsupportedOperationException("Remote auth repo cannot perform getMutableSessionID request")
    }

    override suspend fun connect() {
        tcpWorker.connect()
    }

    override suspend fun disconnect(code: Int) {
        tcpWorker.disconnect(code)
    }

    override suspend fun requestSessionID() {
        tcpWorker.requestSessionID()
    }

    override suspend fun requestTcpIP() {
        udpWorker.requestTcpIP()
    }

}