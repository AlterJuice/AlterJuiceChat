package com.edu.alterjuicechat.repo

interface AuthRepo {

    fun passwordIsValid(password: String): Boolean
    fun userIsLoggedIn(): Boolean
    fun registerUser(name: String)


}