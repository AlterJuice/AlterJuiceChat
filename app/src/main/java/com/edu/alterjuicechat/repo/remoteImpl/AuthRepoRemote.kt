package com.edu.alterjuicechat.repo.remoteImpl

import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.repo.AuthRepo

class AuthRepoRemote(
    private val tcpWorker: TCPWorker,
    private val udpWorker: UDPWorker
) : AuthRepo {

    override fun saveUsername(username: String) {
        throw UnsupportedOperationException("Remote auth repo cannot perform saveUsername request")
    }

    override fun getSavedUsername(): String {
        throw UnsupportedOperationException("Remote auth repo cannot perform getUsername request")
    }

    override suspend fun connect() {
        tcpWorker.connect()
    }

    override suspend fun disconnect(code: Int) {
        tcpWorker.disconnect(code)
    }

    override suspend fun requestSessionID() {
        tcpWorker.connectWithTcp()
    }

    override suspend fun requestTcpIP() {
        udpWorker.requestTcpIP()
    }


}