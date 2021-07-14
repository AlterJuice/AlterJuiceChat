package com.edu.alterjuicechat.ui.adapters.items

import com.edu.alterjuicechat.Consts
import java.text.SimpleDateFormat
import java.util.*

class Message(
    val fromID: String,
    val text: String = "History is empty",
    val date: Date = Date(0)
) {

    fun getFormattedDate(): String {
        return SimpleDateFormat(Consts.SIMPLE_MESSAGE_DATE_FORMAT).format(date)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Message)
            return fromID == other.fromID && text == other.text && date.time == other.date.time
        return false
    }

    override fun hashCode(): Int {
        var result = fromID.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}