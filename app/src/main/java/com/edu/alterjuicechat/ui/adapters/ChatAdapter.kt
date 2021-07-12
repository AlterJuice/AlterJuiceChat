package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.data.network.model.Chat
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.ChatsListItemBinding

class ChatAdapter(private val onChatClick: (UserDto) -> Unit) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {
    

    private val chats: ArrayList<Chat> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding: ChatsListItemBinding = ChatsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(onChatClick, chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun addItem(chatItem: Chat){
        chats.add(chatItem)
        notifyItemInserted(chats.size)
    }

    fun addItem(userID: String, username: String, lastMessage: String = "History is empty"){
        addItem(Chat(username, userID, lastMessage))
    }

    fun setChats(users: List<Chat>){
        chats.clear()
        chats.addAll(users)
        notifyDataSetChanged()
    }

    class ChatHolder(private val binding: ChatsListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(onClick: (UserDto) -> Unit, chatItem: Chat){
            binding.root.setOnClickListener { onClick(chatItem.generateUserDto()) }
            binding.chatTitle.text = chatItem.username
            binding.chatLastMessageText.text = chatItem.lastMessage
        }
    }
}