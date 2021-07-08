package com.edu.alterjuicechat.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.databinding.MessagesListItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min


class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {
    val maxMessageSize = 1400

    class Message(private val messageText: String, private val messageDateLong: Long, private val isMine: Boolean){

        fun getIsMine(): Boolean{
            return isMine
        }

        fun getDateStr(): String{
            return "Date: ${Date(messageDateLong*1000)}"
        }
        fun getMessageStr():String{
            return messageText
        }
    }

    private val collection: ArrayList<Message> = ArrayList()

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

    fun addItem(message: Message){
        collection.add(message)
        notifyItemInserted(collection.size)

    }
    fun setItems(messages: List<MessageDto>, sessionID: String){
        collection.clear()
        messages.forEach {
            addItem(Message(it.message, Date().time, it.from.id == sessionID))
        }

    }


    inner class MessageHolder(private val binding: MessagesListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(message: Message){
            binding.messageText.text = message.getMessageStr()
            binding.messageDate.text = message.getDateStr()
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)

            if (message.getIsMine()) {
                constraintSet.connect(
                    R.id.messageCard, ConstraintSet.END,
                    R.id.messageLayoutRoot, ConstraintSet.END
                )
                binding.messageCard.backgroundTintList = binding.root.context.getColorStateList(R.color.message_background_mine)
            }else {
                constraintSet.connect(R.id.messageCard, ConstraintSet.START,
                    R.id.messageLayoutRoot, ConstraintSet.START)

                binding.messageCard.backgroundTintList = binding.root.context.getColorStateList(R.color.message_background_not_mine)
                // binding.messageCard.setBackgroundColor(binding.root.context.getColor(R.color.message_background_not_mine))
            }
            constraintSet.constrainPercentWidth(R.id.messageCard, calculateWidthOnTestSize(message.getMessageStr().length))
            constraintSet.applyTo(binding.root)
        }
        private fun calculateWidthOnTestSize(messageSize: Int): Float{
            val f = min(0.2f+max(0.2f, (messageSize/maxMessageSize).toFloat()), 0.8f)
            Log.d("LoggingPercentageWidth", f.toString())
            return f
        }
    }
}