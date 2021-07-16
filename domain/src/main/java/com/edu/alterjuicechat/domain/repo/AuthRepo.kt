package com.edu.alterjuicechat.domain.repo

interface AuthRepo : ConnectRepo {
    fun saveUsername(username: String)
    fun getSavedUsername(): String
}