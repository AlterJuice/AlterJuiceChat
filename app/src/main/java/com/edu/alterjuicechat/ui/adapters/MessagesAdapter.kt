package com.edu.alterjuicechat.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.databinding.MessagesListItemBinding
import kotlin.math.min


class MessagesAdapter(private val mySessionID: String) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {
    val maxMessageSize = 1400f

    private val collection: ArrayList<MessageDto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val binding: MessagesListItemBinding = MessagesListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MessageHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    fun addItem(message: MessageDto){
        collection.add(message)
        notifyItemInserted(collection.size)
    }

    fun setItems(messages: List<MessageDto>){
        collection.clear()
        collection.addAll(messages)
        notifyDataSetChanged()
    }


    inner class MessageHolder(private val binding: MessagesListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(message: MessageDto){
            binding.messageText.text = message.message
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)

            if (message.from.id == mySessionID) {
                constraintSet.connect(R.id.messageCard, ConstraintSet.END, R.id.messageLayoutRoot, ConstraintSet.END)
                binding.messageCard.backgroundTintList = getColorStateList(R.color.message_background_mine)
            }else {
                constraintSet.connect(R.id.messageCard, ConstraintSet.START, R.id.messageLayoutRoot, ConstraintSet.START)
                binding.messageCard.backgroundTintList = getColorStateList(R.color.message_background_not_mine)
            }
            constraintSet.constrainPercentWidth(R.id.messageCard, calculateWidthOnTestSize(message.message.length))
            constraintSet.applyTo(binding.root)
        }
        private fun calculateWidthOnTestSize(messageSize: Int): Float{
            return min(0.2f+(messageSize/maxMessageSize)*1.6f, 0.8f)
        }

        private fun getColorStateList(colorResId: Int): ColorStateList{
            return binding.root.context.getColorStateList(colorResId)
        }
    }
}