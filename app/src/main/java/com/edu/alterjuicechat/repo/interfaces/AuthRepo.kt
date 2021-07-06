package com.edu.alterjuicechat.repo.interfaces

interface AuthRepo {

    fun passwordIsValid(password: String): Boolean
    fun userIsLoggedIn(): Boolean
    fun authorizeUser(name: String)


}