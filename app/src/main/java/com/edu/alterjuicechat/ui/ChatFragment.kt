package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.FragmentChatBinding
import com.edu.alterjuicechat.ui.adapters.MessagesAdapter
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val vm by viewModel<ChatViewModel>(){ parametersOf(sessionID, user.id) }

    private val user by lazy {
        UserDto(arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: "",
            arguments?.getString(Consts.FRAGMENT_PARAM_USER_NAME)?: Consts.BLANK_USERNAME_PLACEHOLDER)
    }
    private val sessionID by lazy {
        arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)
    }

    private val messagesAdapter by lazy{ MessagesAdapter(sessionID!!) }
    private val chatTitle by lazy{ arguments?.getString(Consts.FRAGMENT_PARAM_CHAT_TITLE)?: Consts.BLANK_TITLE_PLACEHOLDER }

    override fun onDestroy() {
        // Setting unread counter to 0 when exiting chat fragment
        vm.clearUnreadCounter(user.id)
        super.onDestroy()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChatTitle(chatTitle)
        binding.includedMessagesList.messagesList.apply {
            adapter = messagesAdapter
            if (layoutManager == null)
                //
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
        vm.messages.observe(viewLifecycleOwner, {
            messagesAdapter.setItems(it)
            messagesAdapter.notifyDataSetChanged()

            // Do not forget to remove line and swap to ListAdapter
            // *******************************************************************************
            binding.includedMessagesList.messagesList.scrollToPosition(messagesAdapter.itemCount-1)
            binding.chatInputMessage.text.clear()
        })
        vm.clearUnreadCounter(user.id)
        binding.chatInputSendButton.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage(){
        val msgText = binding.chatInputMessage.text.toString()
        if (msgText.isNotBlank()) {
            vm.sendMessage(msgText)
        }
    }

    private fun setChatTitle(title: String){
        binding.chatHeaderTitle.text = title
    }

    companion object {
        @JvmStatic
        fun newInstance(sessionID: String, userId: String, userName: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(Consts.FRAGMENT_PARAM_CHAT_TITLE, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_NAME, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_ID, userId)
                    putString(Consts.FRAGMENT_PARAM_SESSION_ID, sessionID)
                }
            }
    }
}