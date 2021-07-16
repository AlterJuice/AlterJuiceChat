package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.edu.alterjuicechat.databinding.MessageItemMineBinding
import com.edu.alterjuicechat.databinding.MessageItemNotMineBinding
import com.edu.alterjuicechat.socket.dto.entities.MessageDto

private const val VIEW_TYPE_MESSAGE_IS_MINE = 0
private const val VIEW_TYPE_MESSAGE_IS_NOT_MINE = 1

class MessagesAdapter(private val mySessionID: String) : ListAdapter<MessageDto, MessagesAdapter.BaseMessageViewHolder<ViewBinding>>(MessageDifferenceCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder<ViewBinding> {

        return if (viewType == VIEW_TYPE_MESSAGE_IS_MINE){
            MineMessagesHolder(MessageItemMineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        }else{
            NotMineMessagesHolder(MessageItemNotMineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        } as BaseMessageViewHolder<ViewBinding>
    }


    override fun getItemViewType(position: Int): Int {
        if (getItem(position).from.id == mySessionID)
            return VIEW_TYPE_MESSAGE_IS_MINE
        return VIEW_TYPE_MESSAGE_IS_NOT_MINE
    }

    override fun onBindViewHolder(holder: BaseMessageViewHolder<ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageDifferenceCallback : DiffUtil.ItemCallback<MessageDto>() {
        override fun areContentsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.from.id == newItem.from.id && oldItem.message == newItem.message
        }

        override fun areItemsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.from.id == newItem.from.id && oldItem.message == newItem.message
        }
    }


    abstract class BaseMessageViewHolder<VB: ViewBinding>(protected val binding: VB) : RecyclerView.ViewHolder(binding.root){
        abstract fun bind(message: MessageDto)
    }

    class MineMessagesHolder(binding: MessageItemMineBinding) : BaseMessageViewHolder<MessageItemMineBinding>(binding) {
        override fun bind(message: MessageDto) {
            binding.messageText.text = message.message
        }
    }

    class NotMineMessagesHolder(binding: MessageItemNotMineBinding) : BaseMessageViewHolder<MessageItemNotMineBinding>(binding) {
        override fun bind(message: MessageDto) {
            binding.messageText.text = message.message
        }
    }

}