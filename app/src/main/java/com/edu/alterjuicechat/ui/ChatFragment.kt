package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.model.dto.UserDto
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo
import org.koin.android.ext.android.get


class ChatFragment : Fragment() {

    private val messagesRepo: MessagesRepo = get()

    private val user by lazy {
        UserDto(arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: "",
            arguments?.getString(Consts.FRAGMENT_PARAM_USER_NAME)?: "")
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


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
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