package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.repo.ChatListRepo
import com.edu.alterjuicechat.socket.DataStore
import com.edu.alterjuicechat.socket.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatListRepoDecorator: ChatListRepo,
    dataStore: DataStore
) : ViewModel() {
    val users: Flow<List<UserInfo>> = dataStore.getMutableUsers()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true){
                chatListRepoDecorator.requestUsers()
                delay(Consts.USERS_LOOP_UPDATER_DELAY)
            }
        }
    }

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            chatListRepoDecorator.requestUsers()
        }
    }

}