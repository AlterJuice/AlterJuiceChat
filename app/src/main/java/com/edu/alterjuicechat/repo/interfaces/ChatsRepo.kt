package com.edu.alterjuicechat.repo.interfaces

interface ChatsRepo {

    fun saveMessage(id: String, text: String)
}