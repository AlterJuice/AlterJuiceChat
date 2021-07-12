package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.alterjuicechat.data.network.DataStore
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.ChatListRepo
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatListRepoDecorator: ChatListRepo,
    dataStore: DataStore
) : ViewModel() {
    val users: LiveData<List<ChatAdapter.ChatItem>> = dataStore.mutableUsers

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            chatListRepoDecorator.requestUsers()
        }
    }
}