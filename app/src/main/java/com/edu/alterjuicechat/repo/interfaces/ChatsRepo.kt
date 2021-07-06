package com.edu.alterjuicechat.repo.interfaces

import com.edu.alterjuicechat.data.network.model.dto.UserDto

interface ChatsRepo {

    fun getChats(): List<UserDto>

}