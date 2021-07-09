package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.ChatListRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel(
    private val chatListRepoDecorator: ChatListRepo,
    private val sessionID: String,
) : ViewModel() {
    private val liveUsers: MutableLiveData<List<UserDto>> = MutableLiveData()
    val users: LiveData<List<UserDto>> = liveUsers

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            val users = chatListRepoDecorator.getUsers(sessionID)
            withContext(Dispatchers.Main){
                liveUsers.value = users
            }
        }
    }
}