package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val vm by viewModel<AuthViewModel>()

    private fun openChatListFragment(sessionID: String, username: String){
        with(requireActivity() as MainActivity) {
            replaceFragment(ChatListFragment.newInstance(sessionID, username), "Chat", false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appStartMessaging.setOnClickListener(::onConnectClick)
        vm.liveTcpIP.observe(viewLifecycleOwner, {
            processStepWithTcpIP(it)
        })
        vm.liveSessionID.observe(viewLifecycleOwner, {
            processStepWithSessionID(it)
        })
        // processNextStepWithIp(arguments?.getString())
        // onLogInClick()
    }

    private fun onConnectClick(v: View){
        Toast.makeText(context, getString(R.string.toast_searching_the_server), Toast.LENGTH_LONG).show()
        showUIProgressIsLoading(true)
        setStartMessagingEnabled(false)
        vm.flowGetTCPIpFromUDP()
    }

    private fun processStepWithSessionID(sessionID: String){
        if (sessionID.isNotEmpty()) {
            showUIProgressIsLoading(false)
            val username = vm.getSavedUsername()
            openChatListFragment(sessionID, username)
            vm.connect()
            setUIViewsEnabled(true)
        }
    }

    private fun processStepWithTcpIP(tcpIP: String){
        if (tcpIP.isNotEmpty()) {
            showUIProgressIsLoading(false)
            showTCPInfoText(tcpIP)
            processNextStepWithIp(tcpIP)
        }
    }

    private fun showTCPInfoText(tcpIp: String) {
        with(binding.foundTcpIpText) {
            visibility = View.VISIBLE
            text = getString(R.string.found_server_on, tcpIp)
        }
    }

    private fun processNextStepWithIp(tcpIp: String){
        val thisUserName = vm.getSavedUsername()
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

    private fun showUIProgressIsLoading(isVisible: Boolean){
        binding.mainProgressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun onLogInClick(tcpIp: String){
        val inputUsernameText = binding.inputUsername.text.toString()
        if (inputUsernameText.isBlank()){
            Toast.makeText(context, getString(R.string.toast_create_username), Toast.LENGTH_SHORT).show()
            return
        }
        setUIViewsEnabled(false)
        showUIProgressIsLoading(true)
        vm.saveUsername(inputUsernameText)
        vm.flowGetSessionIDFromTCP(tcpIp, inputUsernameText)
    }
}