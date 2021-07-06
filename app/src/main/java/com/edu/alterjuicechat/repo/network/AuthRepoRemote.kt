package com.edu.alterjuicechat.repo.network

import com.edu.alterjuicechat.data.network.NetworkWorker
import com.edu.alterjuicechat.repo.interfaces.AuthRepo

class AuthRepoRemote (
    private val networkWorker: NetworkWorker
        ): AuthRepo {
    override fun passwordIsValid(password: String): Boolean {
        return true
    }

    override fun userIsLoggedIn(): Boolean {
        return true
    }

    override fun authorizeUser(name: String) {
        TODO("Not yet implemented")
    }
}