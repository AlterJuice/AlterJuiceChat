package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R


class ChatsListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chats_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChatsListFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
                }
            }
    }
}