package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.repo.AuthRepo

class AuthRepoDecorator(
    private val localAuthRepo: AuthRepo,
    private val remoteAuthRepo: AuthRepo
): AuthRepo {
    override fun saveUsername(username: String) {
        localAuthRepo.saveUsername(username)
    }

    override fun getSavedUsername(): String {
        return localAuthRepo.getSavedUsername()
    }

    override suspend fun connect(sessionID: String, username: String) {
        remoteAuthRepo.connect(sessionID, username)
    }

    override suspend fun disconnect(sessionID: String, code: Int) {
        remoteAuthRepo.disconnect(sessionID, code)
    }

    override suspend fun getSessionID(tcpIP: String, username: String): String {
        return remoteAuthRepo.getSessionID(tcpIP, username)
    }

    override suspend fun getTcpIP(): String {
        return remoteAuthRepo.getTcpIP()
    }

}