package com.edu.alterjuicechat.repo.localImpl

import android.content.SharedPreferences
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.repo.AuthRepo

class AuthRepoLocal(
    private val profilePrefs: SharedPreferences,
    ) : AuthRepo {

    override fun saveUsername(username: String) {
        profilePrefs.edit().putString(Consts.PROFILE_KEY_NAME, username).apply()
    }

    override fun getSavedUsername(): String {
        return profilePrefs.getString(Consts.PROFILE_KEY_NAME, "")!!
    }

    override suspend fun connect(sessionID: String, username: String) {
        throw UnsupportedOperationException("Local auth repo cannot perform connect request")
    }

    override suspend fun disconnect(sessionID: String, code: Int) {
        throw UnsupportedOperationException("Local auth repo cannot perform disconnect request")
    }

    override suspend fun getSessionID(tcpIP: String, username: String): String {
        throw UnsupportedOperationException("Local auth repo cannot perform getSessionID request")
    }

    override suspend fun getTcpIP(): String {
        throw UnsupportedOperationException("Local auth repo cannot perform getTcpIP request")
    }
}