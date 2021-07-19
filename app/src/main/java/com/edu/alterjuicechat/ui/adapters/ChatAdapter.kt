package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.databinding.ChatsListItemBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.model.User
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDate(milliseconds: Long): String{
    return SimpleDateFormat(Consts.SIMPLE_MESSAGE_DATE_FORMAT).format(Date(milliseconds))
}

class ChatAdapter(private val onChatClick: (User) -> Unit) :
    ListAdapter<User, ChatAdapter.ChatHolder>(UserDifferenceCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding: ChatsListItemBinding = ChatsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(onChatClick, getItem(position))
    }

    class ChatHolder(private val binding: ChatsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onClick: (User) -> Unit, chatItem: User) {
            binding.root.setOnClickListener { onClick(chatItem) }
            binding.chatTitle.text = chatItem.chatName
            binding.chatLastMessageText.text = if (chatItem.lastMessage.isEmpty()) "History is empty" else chatItem.lastMessage
            // Show last message date. if date.seconds is 0 -> set view as invisible
            if (chatItem.lastMessageDateMilliseconds != 0L) {
                binding.chatLastMessageDate.visibility = View.VISIBLE
                binding.chatLastMessageDate.text = getFormattedDate(chatItem.lastMessageDateMilliseconds)
            } else {
                binding.chatLastMessageDate.visibility = View.INVISIBLE
            }

            // Show counter of unread messages. if 0 -> set view as invisible
            if (chatItem.unreadMessagesCount == 0) {
                binding.chatCountUnreadMessages.visibility = View.INVISIBLE
            } else {
                binding.chatCountUnreadMessages.visibility = View.VISIBLE
                binding.chatCountUnreadMessages.text = chatItem.unreadMessagesCount.toString()
            }
        }
    }

    class UserDifferenceCallback : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return false
            // return oldItem.lastMessage == newItem.lastMessage
            //         && oldItem.countUnreadMessages == newItem.countUnreadMessages
            //         && oldItem.username == newItem.username
            //         && oldItem.userID == newItem.userID
        }

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.chatID == newItem.chatID
        }
    }
}