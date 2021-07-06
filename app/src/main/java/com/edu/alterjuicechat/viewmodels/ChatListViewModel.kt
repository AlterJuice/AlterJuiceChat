package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.interfaces.ChatsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListViewModel (private val chatsRepoDecorator: ChatsRepo
) : ViewModel() {
    private val liveUsers: MutableLiveData<List<UserDto>> = MutableLiveData()
    val users: LiveData<List<UserDto>> = liveUsers

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                liveUsers.value = chatsRepoDecorator.getChats()
            }
        }
    }
}