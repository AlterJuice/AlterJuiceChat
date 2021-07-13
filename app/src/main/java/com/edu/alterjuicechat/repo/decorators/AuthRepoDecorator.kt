package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.repo.AuthRepo

class AuthRepoDecorator(
    private val dataStore: DataStore,
    private val localAuthRepo: AuthRepo,
    private val remoteAuthRepo: AuthRepo
) : AuthRepo {
    override fun saveUsername(username: String) {
        localAuthRepo.saveUsername(username)
        dataStore.mutableUsername.postValue(username)
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