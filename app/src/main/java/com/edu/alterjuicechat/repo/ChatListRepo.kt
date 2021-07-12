package com.edu.alterjuicechat.repo

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.model.dto.UserDto

interface ChatListRepo {
    suspend fun requestUsers()

}