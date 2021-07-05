package com.edu.alterjuicechat.repo.localImpl

import com.edu.alterjuicechat.repo.interfaces.AuthRepo

class AuthRepoLocal: AuthRepo {
    override fun passwordIsValid(password: String): Boolean {

        TODO("Not yet implemented")
    }

    override fun userIsLoggedIn(): Boolean {
        return false
//        TODO("Not yet implemented")
    }

    override fun registerUser(name: String) {
        TODO("Not yet implemented")
    }
}