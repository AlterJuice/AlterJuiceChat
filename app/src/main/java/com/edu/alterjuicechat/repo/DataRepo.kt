package com.edu.alterjuicechat.repo

interface DataRepo {

    fun saveMessage(id: String, text: String)
}