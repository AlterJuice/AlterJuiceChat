package com.edu.alterjuicechat.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.NetworkWorker
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.security.auth.callback.Callback

class AuthViewModel(
    private val profilePreferences: SharedPreferences,
    private val tcpWorker: TCPWorker,
    private val udpWorker: UDPWorker
) : ViewModel() {


    fun getSavedUsername(): String{
        return profilePreferences.getString(Consts.PROFILE_KEY_NAME, "")!!
    }

    fun saveUsername(username: String){
        profilePreferences.edit().putString(Consts.PROFILE_KEY_NAME, username).apply()
    }

    fun connect(sessionID: String, username: String){
        viewModelScope.launch {
            flow {
                emit(tcpWorker.connect(sessionID, username))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    fun flowGetTCPIpFromUDP(runnable: (String) -> Unit){
        viewModelScope.launch {
            flow { emit(udpWorker.getTcpIp()) }
                .flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main){
                        runnable(it)
                    }
                }
        }
    }

    fun flowGetSessionIDFromTCP(tcpIp: String, username: String, runnable: (String) -> Unit) {
        viewModelScope.launch {
            flow { emit(tcpWorker.connectWithTcp(tcpIp, username)) }
                .flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main){
                        runnable(it)
                    }
                }
        }
    }
}