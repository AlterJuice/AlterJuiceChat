package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.repo.AuthRepo
import kotlinx.coroutines.*

class AuthViewModel(
    private val authRepoDecorator: AuthRepo
) : ViewModel() {
    private val privateTcpIP: MutableLiveData<String> = MutableLiveData()
    private val privateSessionID: MutableLiveData<String> = MutableLiveData()
    val liveTcpIP: LiveData<String> = privateTcpIP
    val liveSessionID: LiveData<String> = privateSessionID


    fun getSavedUsername(): String = authRepoDecorator.getSavedUsername()
    fun saveUsername(username: String) = authRepoDecorator.saveUsername(username)

    fun connect(){
        val sessionID = liveSessionID.value as String
        val username = getSavedUsername()
        connect(sessionID, username)
    }

    fun connect(sessionID: String, username: String){
        viewModelScope.launch(Dispatchers.IO){
            if (sessionID.isNotBlank() && username.isNotBlank()){
                authRepoDecorator.connect(sessionID, username)
            }
        }
    }

    fun disconnect(code: Int){
        disconnect(liveSessionID.value!!, code)
    }

    fun disconnect(sessionID: String, code: Int){
        viewModelScope.launch(Dispatchers.IO){
            if (sessionID.isNotBlank()){
                authRepoDecorator.disconnect(sessionID, code)
            }
        }
    }

    fun flowGetTCPIpFromUDP(){
        viewModelScope.launch(Dispatchers.IO){
            val ip = authRepoDecorator.getTcpIP()
            withContext(Dispatchers.Main){
                privateTcpIP.value = ip
            }
        }
    }

    fun flowGetSessionIDFromTCP(tcpIP: String, username: String) {
        viewModelScope.launch(Dispatchers.IO){
            val sessionID = authRepoDecorator.getSessionID(tcpIP, username)
            withContext(Dispatchers.Main){
                privateSessionID.value = sessionID
            }
        }
    }

    // viewModelScope.launch {
    //     flow { emit(udpWorker.getTcpIp()) }
    //         .flowOn(Dispatchers.IO)
    //         .collect {
    //             withContext(Dispatchers.Main){
    //                 runnable(it)
    //             }
    //         }
    // }
}