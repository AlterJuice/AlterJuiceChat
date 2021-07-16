package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentChatsListBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.socket.UserInfo
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import com.edu.alterjuicechat.ui.base.BaseFragment
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
        binding.imageUpdateChats.setOnClickListener {
            vm.loadUsers()
            Toast.makeText(context, R.string.toast_users_updated, Toast.LENGTH_SHORT).show()
        }
        vm.users.observe(viewLifecycleOwner, {
            chatsAdapter.submitList(it + UserInfo("CustomID", "TestChatName"))
        })
    }

    private fun onChatClick(userInfo: UserInfo){
        replaceFragment(ChatFragment.newInstance(sessionID, userInfo.chatID, userInfo.chatName), Consts.FRAGMENT_TAG_PRIVATE_CHAT, true)
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