package com.edu.alterjuicechat.ui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : BaseFragment() {
    private lateinit var binding: FragmentAuthBinding
    private val vm by viewModel<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animationDrawable = binding.root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()


        binding.appStartMessaging.setOnClickListener{ onStartClick()}
        vm.liveTcpIP.observe(viewLifecycleOwner, {
            // it is new TCP IP
            if (it.isNotEmpty()) {
                showUIProgressIsLoading(false)
                val thisUserName = vm.getSavedUsername()
                binding.foundTcpIpText.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.found_server_on, it)
                }
                binding.buttonSignIn.visibility = View.VISIBLE
                binding.textInputUsername.setText(thisUserName)
                binding.buttonSignIn.setOnClickListener { onLogInClick() }
            }
        })
        vm.liveSessionID.observe(viewLifecycleOwner, {
            // it is new SessionID
            if (it.isNotEmpty()) {
                showUIProgressIsLoading(false)
                val username = vm.getSavedUsername()
                openChatListFragment(it, username)
                vm.connect()
                setUIViewsEnabled(true)
            }
        })
    }

    private fun onStartClick(){
        Toast.makeText(context, getString(R.string.toast_searching_the_server), Toast.LENGTH_LONG).show()
        showUIProgressIsLoading(true)
        setStartMessagingEnabled(false)
        vm.requestTcpIPFromUdp()
    }

    private fun openChatListFragment(sessionID: String, username: String){
        replaceFragment(ChatListFragment.newInstance(sessionID, username), Consts.FRAGMENT_TAG_CHAT_LIST, false)
    }


    private fun setStartMessagingEnabled(enabled: Boolean){
        binding.appStartMessaging.isEnabled = enabled
    }

    private fun setUIViewsEnabled(enabled: Boolean){
        setStartMessagingEnabled(enabled)
        binding.buttonSignIn.isEnabled = enabled
        binding.textInputUsername.isEnabled = enabled
    }

    private fun showUIProgressIsLoading(isVisible: Boolean){
        binding.mainProgressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun onLogInClick(){
        val inputUsernameText = binding.textInputUsername.text.toString()
        if (inputUsernameText.isBlank()){
            Toast.makeText(context, getString(R.string.toast_create_username), Toast.LENGTH_SHORT).show()
            return
        }
        setUIViewsEnabled(false)
        showUIProgressIsLoading(true)
        vm.saveUsername(inputUsernameText)
        vm.requestSessionIDFromTCP()
    }
}