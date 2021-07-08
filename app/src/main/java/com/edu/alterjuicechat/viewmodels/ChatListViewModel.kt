package com.edu.alterjuicechat.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.data.network.model.dto.UsersReceivedDto
import com.edu.alterjuicechat.repo.interfaces.ChatsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel(
    private val tcpWorker: TCPWorker,
    private val sessionID: String,

) : ViewModel() {
    private val liveUsers: MutableLiveData<List<UserDto>> = MutableLiveData()

    val users: LiveData<List<UserDto>> = liveUsers

    fun loadUsers(){ // runnable: (List<UserDto>) -> Unit
        viewModelScope.launch {
            flow<UsersReceivedDto> {
                emit(tcpWorker.getUsers(sessionID))
            }.flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main){
                        println(it.users)
                        liveUsers.value = it.users
                    }
                }
        }

    }
}