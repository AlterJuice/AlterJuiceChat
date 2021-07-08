package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.ChatsListItemBinding

class ChatAdapter(private val onChatClick: (UserDto) -> Unit) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val chats: ArrayList<UserDto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding: ChatsListItemBinding = ChatsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(chats.get(position))
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun addItem(userDto: UserDto){
        chats.add(userDto)
        notifyItemInserted(chats.size)
    }

    fun addItem(userID: String, username: String){
        addItem(UserDto(userID, username))
    }
    fun setChats(users: List<UserDto>){
        chats.clear()
        chats.addAll(users)
        notifyDataSetChanged()
    }

    inner class ChatHolder(private val binding: ChatsListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(userDto: UserDto){
            binding.root.setOnClickListener { onChatClick(userDto) }
            binding.chatTitle.text = userDto.name
            binding.chatLastMessageText.text = "History is empty"
            // binding.chatLastMessageDate
        }
    }
}