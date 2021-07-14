package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.data.network.dto.model.UserDto
import com.edu.alterjuicechat.databinding.ChatsListItemBinding
import com.edu.alterjuicechat.ui.adapters.items.Chat

class ChatAdapter(private val onChatClick: (UserDto) -> Unit) :
    ListAdapter<Chat, ChatAdapter.ChatHolder>(UserDifferenceCallback()) {

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
        fun bind(onClick: (UserDto) -> Unit, chatItem: Chat) {
            binding.root.setOnClickListener { onClick(chatItem.generateUserDto()) }
            binding.chatTitle.text = chatItem.username
            binding.chatLastMessageText.text = chatItem.lastMessage.text
            // Show last message date. if date.seconds is 0 -> set view as invisible
            if (chatItem.lastMessage.date.time != 0L) {
                binding.chatLastMessageDate.visibility = View.VISIBLE
                binding.chatLastMessageDate.text = chatItem.getFormattedDate()
            } else {
                binding.chatLastMessageDate.visibility = View.INVISIBLE
            }

            // Show counter of unread messages. if 0 -> set view as invisible
            if (chatItem.countUnreadMessages == 0) {
                binding.chatCountUnreadMessages.visibility = View.INVISIBLE
            } else {
                binding.chatCountUnreadMessages.visibility = View.VISIBLE
                binding.chatCountUnreadMessages.text = chatItem.countUnreadMessages.toString()
            }
        }
    }

    class UserDifferenceCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return false
            // return oldItem.lastMessage == newItem.lastMessage
            //         && oldItem.countUnreadMessages == newItem.countUnreadMessages
            //         && oldItem.username == newItem.username
            //         && oldItem.userID == newItem.userID
        }

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.userID == newItem.userID
        }
    }
}