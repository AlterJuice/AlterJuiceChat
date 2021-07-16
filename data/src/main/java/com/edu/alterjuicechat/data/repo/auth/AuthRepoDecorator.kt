package com.edu.alterjuicechat.data.repo.auth

import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.socket.DataStore

internal class AuthRepoDecorator(
    private val dataStore: DataStore,
    private val localAuthRepo: AuthRepo,
    private val remoteAuthRepo: AuthRepo
) : AuthRepo {
    override fun saveUsername(username: String) {
        localAuthRepo.saveUsername(username)
        dataStore.getMutableUsername().postValue(username)
    }

    override fun getSavedUsername(): String {
        return localAuthRepo.getSavedUsername()
    }

    override suspend fun connect() {
        remoteAuthRepo.connect()
    }

    override suspend fun disconnect(code: Int) {
        remoteAuthRepo.disconnect(code)
    }

    override suspend fun requestSessionID() {
        return remoteAuthRepo.requestSessionID()
    }

    override suspend fun requestTcpIP() {
        remoteAuthRepo.requestTcpIP()
    }


}