package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.ui.adapters.items.Chat
import com.edu.alterjuicechat.repo.ChatListRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatListRepoDecorator: ChatListRepo,
    dataStore: DataStore
) : ViewModel() {
    val users: LiveData<List<Chat>> = dataStore.mutableUsers
    private var job: Job? = null

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            chatListRepoDecorator.requestUsers()
        }
    }
    fun startUsersLoopUpdater(){
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true){
                chatListRepoDecorator.requestUsers()
                delay(Consts.USERS_LOOP_UPDATER_DELAY)
            }
        }
    }

    fun stopUsersLoopUpdater(){
        job?.cancel()
    }
}