package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.edu.alterjuicechat.databinding.MessageItemMineBinding
import com.edu.alterjuicechat.databinding.MessageItemNotMineBinding
import com.edu.alterjuicechat.ui.adapters.items.Message

private const val VIEW_TYPE_MESSAGE_IS_MINE = 0
private const val VIEW_TYPE_MESSAGE_IS_NOT_MINE = 1

class MessagesAdapter(private val mySessionID: String) : ListAdapter<Message, MessagesAdapter.BaseMessageViewHolder<ViewBinding>>(MessageDifferenceCallback()){
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
        // (collection[position].from.id == mySessionID).compareTo(true)
        if (getItem(position).fromID == mySessionID)
            return VIEW_TYPE_MESSAGE_IS_MINE
        return VIEW_TYPE_MESSAGE_IS_NOT_MINE
    }


    override fun onBindViewHolder(holder: BaseMessageViewHolder<ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageDifferenceCallback : DiffUtil.ItemCallback<Message>() {
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.text ==  newItem.text && oldItem.fromID == newItem.fromID
                    && oldItem.date.time == newItem.date.time
        }
    }


    abstract class BaseMessageViewHolder<VB: ViewBinding>(protected val binding: VB) : RecyclerView.ViewHolder(binding.root){
        abstract fun bind(message: Message)
    }


    class MineMessagesHolder(binding: MessageItemMineBinding) : BaseMessageViewHolder<MessageItemMineBinding>(binding) {
        override fun bind(message: Message) {
            binding.messageText.text = message.text
        }
    }

    class NotMineMessagesHolder(binding: MessageItemNotMineBinding) : BaseMessageViewHolder<MessageItemNotMineBinding>(binding) {
        override fun bind(message: Message) {
            binding.messageText.text = message.text
        }
    }

}