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


class MessagesAdapter(private val mySessionID: String) : ListAdapter<Message, MessagesAdapter.BaseMessageViewHolder<ViewBinding>>(MessageDifferenceCallback()){
    private val collection: ArrayList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder<ViewBinding> {

        return if (viewType == 0){
            MineMessagesHolder(MessageItemMineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        }else{
            NotMineMessagesHolder(MessageItemNotMineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        } as BaseMessageViewHolder<ViewBinding>
    }


    override fun getItemViewType(position: Int): Int {
        // (collection[position].from.id == mySessionID).compareTo(true)
        return if (collection[position].fromID == mySessionID) 0 else 1
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    fun setItems(messages: List<Message>) {
        collection.clear()
        collection.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseMessageViewHolder<ViewBinding>, position: Int) {
        holder.bind(collection[position])
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