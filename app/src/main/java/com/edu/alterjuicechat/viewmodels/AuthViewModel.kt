package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.repo.AuthRepo
import kotlinx.coroutines.*

class AuthViewModel(
    private val authRepoDecorator: AuthRepo,
    dataStore: DataStore
) : ViewModel() {
    private val privateTcpIP: MutableLiveData<String> = MutableLiveData()
    val liveTcpIP: LiveData<String> = dataStore.mutableTcpIP
    val liveSessionID: LiveData<String> = dataStore.mutableSessionID


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

    fun requestTcpIPFromUdp(){
        viewModelScope.launch(Dispatchers.IO){
            authRepoDecorator.requestTcpIP()
        }
    }
    fun requestSessionIDFromTCP(){
        viewModelScope.launch(Dispatchers.IO){
            authRepoDecorator.requestSessionID()
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