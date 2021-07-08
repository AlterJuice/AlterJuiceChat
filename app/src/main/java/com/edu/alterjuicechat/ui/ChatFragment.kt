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
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.util.*


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val user by lazy {
        UserDto(arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: "",
            arguments?.getString(Consts.FRAGMENT_PARAM_USER_NAME)?: Consts.BLANK_USERNAME_PLACEHOLDER)
    }
    private val sessionID by lazy {
        arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)
    }


    private val messagesAdapter by lazy{ MessagesAdapter() }
    private val chatTitle by lazy{ arguments?.getString(Consts.FRAGMENT_PARAM_CHAT_TITLE)?: Consts.BLANK_TITLE_PLACEHOLDER }


    private val vm by viewModel<ChatViewModel>(named(Consts.VIEW_MODEL_NAME_CHAT))


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
        vm.messages.observe(viewLifecycleOwner, {
            println("NewMessages: $it")
            messagesAdapter.setItems(it, sessionID!!)
            messagesAdapter.notifyDataSetChanged()
            binding.includedMessagesList.messagesList.scrollToPosition(messagesAdapter.itemCount-1)
            binding.chatInputMessage.text.clear()
        })
        binding.chatInputSendButton.setOnClickListener {
            val msgText = binding.chatInputMessage.text.toString()
            if (msgText.isNotBlank()) {
                vm.sendMessage(sessionID!!, user, msgText)
            }
        }
        return binding.root
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