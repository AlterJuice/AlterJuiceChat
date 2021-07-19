package com.edu.alterjuicechat.data.repo.auth

import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.socket.DataStore
import kotlinx.coroutines.flow.Flow

internal class AuthRepoDecorator(
    private val localAuthRepo: AuthRepo,
    private val remoteAuthRepo: AuthRepo
) : AuthRepo {
    override fun saveUsername(username: String) {
        localAuthRepo.saveUsername(username)
    }

    override fun getSavedUsername(): String {
        return localAuthRepo.getSavedUsername()
    }

    override fun getMutableUsername(): Flow<String> {
        return localAuthRepo.getMutableUsername()
    }

    override fun getMutableTcpIP(): Flow<String> {
        return localAuthRepo.getMutableTcpIP()
    }

    override fun getMutableSessionID(): Flow<String> {
        return localAuthRepo.getMutableSessionID()
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