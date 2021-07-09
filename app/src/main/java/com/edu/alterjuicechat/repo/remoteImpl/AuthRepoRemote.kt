package com.edu.alterjuicechat.repo.remoteImpl

import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.repo.AuthRepo

class AuthRepoRemote (
    private val tcpWorker: TCPWorker,
    private val udpWorker: UDPWorker
        ): AuthRepo {

    override fun saveUsername(username: String) {
        throw UnsupportedOperationException("Remote auth repo cannot perform saveUsername request")
    }

    override fun getSavedUsername(): String {
        throw UnsupportedOperationException("Remote auth repo cannot perform getUsername request")
    }

    override suspend fun connect(sessionID: String, username: String) {
        tcpWorker.connect(sessionID, username)
    }

    override suspend fun disconnect(sessionID: String, code: Int) {
        tcpWorker.disconnect(sessionID, code)
    }

    override suspend fun getSessionID(tcpIP: String, username: String): String {
        return tcpWorker.connectWithTcp(tcpIP, username)
    }

    override suspend fun getTcpIP(): String {
        return udpWorker.getTcpIp()
    }

}