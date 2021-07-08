package com.edu.alterjuicechat.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.data.network.UDPWorker
import com.edu.alterjuicechat.databinding.FragmentStartBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.android.ext.android.get
import java.lang.Exception


class StartFragment : Fragment() {
    private var udpWorker: UDPWorker = get()
    private var tcpWorker: TCPWorker = get()
    private val profilePreferences: SharedPreferences = get()
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.appStartMessaging.setOnClickListener(::onConnectClick)
        return binding.root
    }

    private fun onConnectClick(v: View){
        Toast.makeText(context, "Searching the server. Please wait", Toast.LENGTH_LONG).show()
        showUIStartLoading()
        setStartMessagingEnabled(false)
        MainScope().launch {
            flow<String> {
                emit(udpWorker.getTcpIp())
            }.flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main) {
                        binding.foundTcpIpText.visibility = View.VISIBLE
                        binding.foundTcpIpText.text = getString(R.string.found_server_on, it)
                        showUIStopLoading()
                        // setStartMessagingEnabled(true)
                        delay(2000)
                        processNextStepWithIp(tcpIp = it)
                    }
                }
        }
    }
    private fun processNextStepWithIp(tcpIp: String){
        val thisUserName = profilePreferences.getString(Consts.PROFILE_KEY_NAME, "")
        binding.inputSignInField.visibility = View.VISIBLE
        binding.inputUsername.setText(thisUserName)
        binding.buttonSignIn.setOnClickListener { onLogInClick(tcpIp) }
    }

    private fun setStartMessagingEnabled(enabled: Boolean){
        binding.appStartMessaging.isEnabled = enabled
    }

    private fun setUIViewsEnabled(enabled: Boolean){
        setStartMessagingEnabled(enabled)
        binding.buttonSignIn.isEnabled = enabled
        binding.inputUsername.isEnabled = enabled
    }

    private fun showUIStopLoading(){
        binding.mainProgressBar.visibility = View.INVISIBLE
    }

    private fun showUIStartLoading(){
        binding.mainProgressBar.visibility = View.VISIBLE

    }

    private fun onLogInClick(tcpIp: String){
        val inputUsernameText = binding.inputUsername.text.toString()
        if (inputUsernameText.isBlank()){
            Toast.makeText(context, "Create your username for chatting", Toast.LENGTH_SHORT).show()
            return
        }
        setUIViewsEnabled(false)
        showUIStartLoading()
        MainScope().launch {
            flow<String> {
                emit(tcpWorker.connectWithTcp(tcpIp, inputUsernameText))
            }.flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main){
                        showUIStopLoading()
                        with(requireActivity() as MainActivity){
                            replaceFragment(ChatFragment.newInstance(it, inputUsernameText), "Chat", false)
                        }
                        setUIViewsEnabled(true)
                    }
                }
        }
    }
}