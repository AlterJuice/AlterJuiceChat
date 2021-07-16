package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.edu.alterjuicechat.databinding.FragmentChatBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.ui.adapters.MessagesAdapter
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatFragment : BaseFragment(){

    private lateinit var binding: FragmentChatBinding
    private val vm by viewModel<ChatViewModel>(){ parametersOf(receiverID) }


    private val sessionID by lazy {
        arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)?:""
    }
    private val receiverID by lazy{
        arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: ""
    }

    private val messagesAdapter by lazy{ MessagesAdapter(sessionID) }
    private val chatTitle by lazy{ arguments?.getString(Consts.FRAGMENT_PARAM_CHAT_TITLE)?: Consts.BLANK_TITLE_PLACEHOLDER }

    override fun onDestroy() {
        // Setting unread counter to 0 when exiting chat fragment
        vm.clearUnreadCounter()
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
        binding.messagesList.apply {
            adapter = messagesAdapter
            if (layoutManager == null)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
        vm.messages.observe(viewLifecycleOwner, {
            messagesAdapter.submitList(it)
            binding.textInputMessage.text.clear()
        })
        // vm.clearUnreadCounter() // it can be called in onDestroy method. No sense to call it twice
        binding.buttonSendMessage.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage(){
        val msgText = binding.textInputMessage.text.toString()
        if (msgText.isNotBlank()) {
            vm.sendMessage(msgText)
            animateSendButton()
        }
    }
    private fun animateSendButton(){
        binding.buttonSendMessage.animate().scaleY(1.1f).scaleX(1.1f).setDuration(200).withEndAction {
            binding.buttonSendMessage.animate().scaleX(1f).scaleY(1f).start()
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