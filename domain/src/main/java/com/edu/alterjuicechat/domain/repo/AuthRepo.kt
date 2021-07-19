package com.edu.alterjuicechat.domain.repo

import kotlinx.coroutines.flow.Flow

interface AuthRepo : ConnectRepo {
    fun saveUsername(username: String)
    fun getSavedUsername(): String
    fun getMutableUsername(): Flow<String>
    fun getMutableTcpIP(): Flow<String>
    fun getMutableSessionID(): Flow<String>
}