package com.edu.alterjuicechat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.databinding.MessageItemMineBinding
import com.edu.alterjuicechat.databinding.MessageItemNotMineBinding
import com.edu.alterjuicechat.ui.adapters.items.Message


class MessagesAdapter(private val mySessionID: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val maxMessageSize = 1400f

    private val collection: ArrayList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            MineMessagesHolder(MessageItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            NotMineMessagesHolder(MessageItemNotMineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        // (collection[position].from.id == mySessionID).compareTo(true)
        return if (collection[position].fromID == mySessionID) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = collection[position]
        when(holder.itemViewType){
            0 -> (holder as MineMessagesHolder).bind(message)
            1 -> (holder as NotMineMessagesHolder).bind(message)
        }
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    fun addItem(message: Message) {
        collection.add(message)
        notifyItemInserted(collection.size)
    }

    fun setItems(messages: List<Message>) {
        collection.clear()
        collection.addAll(messages)
        notifyDataSetChanged()
    }

    interface MessagesViewHolder {
        fun bind(message: Message)
    }

    class MineMessagesHolder(private val binding: MessageItemMineBinding) :
        RecyclerView.ViewHolder(binding.root), MessagesViewHolder {
        override fun bind(message: Message) {
            binding.messageText.text = message.text
        }
    }

    class NotMineMessagesHolder(private val binding: MessageItemNotMineBinding) :
        RecyclerView.ViewHolder(binding.root), MessagesViewHolder {
        override fun bind(message: Message) {
            binding.messageText.text = message.text
        }

    }


    // inner class MessageHolder(private val binding: MessagesListItemBinding): RecyclerView.ViewHolder(binding.root){
    //
    //     fun bind(message: MessageDto){
    //         binding.messageText.text = message.message
    //         val constraintSet = ConstraintSet()
    //         constraintSet.clone(binding.root)
    //
    //         if (message.from.id == mySessionID) {
    //             constraintSet.connect(R.id.messageCard, ConstraintSet.END, R.id.messageLayoutRoot, ConstraintSet.END)
    //             binding.messageCard.backgroundTintList = getColorStateList(R.color.message_background_mine)
    //         }else {
    //             constraintSet.connect(R.id.messageCard, ConstraintSet.START, R.id.messageLayoutRoot, ConstraintSet.START)
    //             binding.messageCard.backgroundTintList = getColorStateList(R.color.message_background_not_mine)
    //         }
    //         constraintSet.constrainPercentWidth(R.id.messageCard, calculateWidthOnTestSize(message.message.length))
    //         constraintSet.applyTo(binding.root)
    //     }
    //     private fun calculateWidthOnTestSize(messageSize: Int): Float{
    //         return min(0.2f+(messageSize/maxMessageSize)*1.6f, 0.8f)
    //     }
    //
    //     private fun getColorStateList(colorResId: Int): ColorStateList{
    //         return binding.root.context.getColorStateList(colorResId)
    //     }
    // }
}