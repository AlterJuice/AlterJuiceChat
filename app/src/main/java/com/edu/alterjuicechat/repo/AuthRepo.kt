package com.edu.alterjuicechat.repo

interface AuthRepo : ConnectRepo {
    fun saveUsername(username: String)
    fun getSavedUsername(): String
}