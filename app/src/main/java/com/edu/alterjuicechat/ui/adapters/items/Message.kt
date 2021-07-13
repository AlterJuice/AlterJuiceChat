package com.edu.alterjuicechat.ui.adapters.items

import com.edu.alterjuicechat.data.network.model.dto.UserDto
import java.text.SimpleDateFormat
import java.util.*

class Message(
    val fromID: String,
    val text: String = "History is empty",
    val date: Date = Date(0)
) {

    fun getFormattedDate(): String {
        return SimpleDateFormat("k:mm").format(date)
    }
}