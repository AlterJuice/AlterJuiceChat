package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.databinding.FragmentChatsListBinding
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatListFragment : Fragment() {

    private val sessionID by lazy { arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID) }
    private val username by lazy { arguments?.getString(Consts.PROFILE_KEY_NAME, Consts.BLANK_USERNAME_PLACEHOLDER) }

    private val vm by viewModel<ChatListViewModel>(){
        parametersOf(sessionID)
    }
    private val chatsAdapter by lazy { ChatAdapter(::onChatClick) }
    private lateinit var binding: FragmentChatsListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.includedChatsList.chatsList){
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.buttonUpdateChats.setOnClickListener {
            vm.loadUsers()
            Toast.makeText(context, "Users updated!", Toast.LENGTH_SHORT).show()
        }
        // Moving connect request to auth fragment before ChatListFragment::newInstance call
        vm.users.observe(viewLifecycleOwner, {
            chatsAdapter.setChats(it)
            chatsAdapter.addItem("CustomID", "TestChat")
        })
        vm.loadUsers()
    }

    private fun onChatClick(userDto: UserDto){
        with(requireActivity() as MainActivity){
            replaceFragment(ChatFragment.newInstance(sessionID!!, userDto.id, userDto.name), "PrivateChat", true)
        }
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