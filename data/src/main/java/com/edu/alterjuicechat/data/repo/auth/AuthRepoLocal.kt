package com.edu.alterjuicechat.data.repo.auth

import android.content.SharedPreferences
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.repo.AuthRepo

internal class AuthRepoLocal(
    private val profilePrefs: SharedPreferences,
) : AuthRepo {

    override fun saveUsername(username: String) {
        profilePrefs.edit().putString(Consts.PROFILE_KEY_NAME, username).apply()
    }

    override fun getSavedUsername(): String {
        return profilePrefs.getString(Consts.PROFILE_KEY_NAME, "")!!
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