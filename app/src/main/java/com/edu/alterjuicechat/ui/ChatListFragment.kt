package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.FragmentChatsListBinding
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import com.edu.alterjuicechat.ui.adapters.items.Chat
import com.edu.alterjuicechat.ui.adapters.items.Message
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatListFragment : BaseFragment() {
    private lateinit var binding: FragmentChatsListBinding
    private val vm by viewModel<ChatListViewModel>(){ parametersOf(sessionID) }
    private val sessionID by lazy { arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)?: "" }
    private val chatsAdapter by lazy { ChatAdapter(::onChatClick) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.chatsList){
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        binding.buttonUpdateChats.setOnClickListener {
            vm.loadUsers()
            Toast.makeText(context, R.string.toast_users_updated, Toast.LENGTH_SHORT).show()
        }
        // Moving connect request to auth fragment before ChatListFragment::newInstance call
        vm.users.observe(viewLifecycleOwner, {
            chatsAdapter.submitList(it + Chat("TestChat", "CustomID", Message("CustomID")))
        })
    }

    private fun onChatClick(userDto: UserDto){
        replaceFragment(ChatFragment.newInstance(sessionID, userDto.id, userDto.name), Consts.FRAGMENT_TAG_PRIVATE_CHAT, true)
    }

    companion object {
        @JvmStatic
        fun newInstance(sessionID: String, username: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(Consts.FRAGMENT_PARAM_SESSION_ID, sessionID)
                    putString(Consts.PROFILE_KEY_NAME, username)
                }
            }
    }
}