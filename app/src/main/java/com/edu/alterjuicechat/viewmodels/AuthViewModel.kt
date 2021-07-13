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
    val liveTcpIP: LiveData<String> = dataStore.mutableTcpIP
    val liveSessionID: LiveData<String> = dataStore.mutableSessionID


    fun getSavedUsername(): String = authRepoDecorator.getSavedUsername()
    fun saveUsername(username: String) = authRepoDecorator.saveUsername(username)

    fun connect(){
        viewModelScope.launch(Dispatchers.IO){
            authRepoDecorator.connect()
        }
    }

    fun disconnect(code: Int){
        viewModelScope.launch(Dispatchers.IO){
            authRepoDecorator.disconnect(code)
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