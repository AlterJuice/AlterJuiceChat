package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.FragmentChatBinding
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo
import com.edu.alterjuicechat.ui.adapters.MessagesAdapter
import org.koin.android.ext.android.get
import java.util.*


class ChatFragment : Fragment() {

    private val messagesRepo: MessagesRepo = get()
    private lateinit var binding: FragmentChatBinding

    private val user by lazy {
        UserDto(arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: "",
            arguments?.getString(Consts.FRAGMENT_PARAM_USER_NAME)?: "")
    }

    private val messagesAdapter by lazy{
        MessagesAdapter()
    }

    private val chatTitle by lazy{
        arguments?.getString(Consts.FRAGMENT_PARAM_CHAT_TITLE)?: "No title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChatTitle(chatTitle)
    }

    private fun setChatTitle(title: String){
        binding.chatHeaderTitle.text = title
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.includedMessagesList.messagesList.apply {
            adapter = messagesAdapter
            if (layoutManager == null)
                layoutManager = LinearLayoutManager(context)
        }
        binding.chatInputSendButton.setOnClickListener {
            val msgText = binding.chatInputMessage.text
            if (msgText.isNotEmpty()) {
                messagesAdapter.addItem(msgText.toString(), Date().time/1000)
                binding.includedMessagesList.messagesList.scrollToPosition(messagesAdapter.itemCount-1)
                binding.chatInputMessage.text.clear()
            }
        }
        return binding.root
    }



    companion object {
        @JvmStatic
        fun newInstance(userId: String, userName: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(Consts.FRAGMENT_PARAM_CHAT_TITLE, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_NAME, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_ID, userId)
                }
            }
    }
}