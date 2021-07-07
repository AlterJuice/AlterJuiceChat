package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.model.dto.UdpDto
import com.edu.alterjuicechat.repo.interfaces.AuthRepo
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class AuthFragment : Fragment() {

    private val authRepo: AuthRepo = get()
    private val viewModel by sharedViewModel<ChatViewModel>(named("1"))

    private val gson: Gson = get()


    private lateinit var t: Thread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authRepo.userIsLoggedIn()){
            (requireActivity() as MainActivity).replaceFragment(ChatsListFragment.newInstance())
            onDestroy()
        }


        // viewModel.sendMessage("RECEIVER VM", "MESSAGEE")
        // viewModel.getUsers()



        // worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
    }

    override fun onDestroy() {
        super.onDestroy()
        t.interrupt()
    }

    fun onConnectClick(){
        viewModel.network.start()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.buttonSendMessage).setOnClickListener {
            viewModel.sendMessage("RECEIVER VM", "MESSAGEE")
            // worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
        }
        view.findViewById<Button>(R.id.buttonConnect).setOnClickListener {
            onConnectClick()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AuthFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }
}