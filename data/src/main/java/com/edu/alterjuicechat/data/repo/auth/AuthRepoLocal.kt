package com.edu.alterjuicechat.data.repo.auth

import android.content.SharedPreferences
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.repo.AuthRepo
import com.edu.alterjuicechat.socket.DataStore
import kotlinx.coroutines.flow.Flow

internal class AuthRepoLocal(
    private val dataStore: DataStore,
    private val profilePrefs: SharedPreferences,
) : AuthRepo {

    override fun saveUsername(username: String) {
        profilePrefs.edit().putString(Consts.PROFILE_KEY_NAME, username).apply()
        dataStore.setUsername(username)
    }

    override fun getSavedUsername(): String {
        return profilePrefs.getString(Consts.PROFILE_KEY_NAME, "")!!
    }

    override fun getMutableUsername(): Flow<String> {
        return dataStore.getMutableUsername()
    }

    override fun getMutableTcpIP(): Flow<String> {
        return dataStore.getMutableTcpIp()
    }

    override fun getMutableSessionID(): Flow<String> {
        return dataStore.getMutableSessionID()
    }

    override suspend fun connect() {
        throw UnsupportedOperationException("Local auth repo cannot perform connect request")
    }

    override suspend fun disconnect(code: Int) {
        throw UnsupportedOperationException("Local auth repo cannot perform disconnect request")
    }

    override suspend fun requestSessionID() {
        throw UnsupportedOperationException("Local auth repo cannot perform getSessionID request")
    }

    override suspend fun requestTcpIP() {
        throw UnsupportedOperationException("Local auth repo cannot perform getTcpIP request")
    }

}