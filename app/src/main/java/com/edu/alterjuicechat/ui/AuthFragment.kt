package com.edu.alterjuicechat.ui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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


        binding.appStartMessaging.setOnClickListener { onStartClick() }
        lifecycleScope.launch(Dispatchers.Main){
            // it is new TCP IP
            vm.liveTcpIP.collect {
                if (it.isNotEmpty()) {
                    showUIProgressIsLoading(false)
                    with(binding.mainAuthForm) {
                        alpha = 0f
                        scaleX = 0.7f
                        scaleY = 0.7f
                        visibility = View.VISIBLE
                        animate().setDuration(500).scaleX(1f).scaleY(1f).alpha(1f).start()
                    }
                    val thisUserName = vm.getSavedUsername()
                    binding.foundTcpIpText.text = getString(R.string.found_server_on, it)
                    binding.textInputUsername.setText(thisUserName)
                    binding.buttonSignIn.setOnClickListener { onLogInClick() }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main){
            // it is new TCP IP
            vm.liveSessionID.collect {
                // it is new SessionID
                if (it.isNotEmpty()) {
                    showUIProgressIsLoading(false)
                    val username = vm.getSavedUsername()
                    openChatListFragment(it, username)
                    vm.connect()
                    setUIViewsEnabled(true)
                }
            }
        }
    }

    private fun onStartClick() {
        setStartMessagingEnabled(false)
        binding.appStartMessaging.animate().scaleY(0f).scaleX(0f).setDuration(300).start()
        Toast.makeText(context, getString(R.string.toast_searching_the_server), Toast.LENGTH_LONG).show()
        showUIProgressIsLoading(true)
        vm.requestTcpIPFromUdp()
    }

    private fun openChatListFragment(sessionID: String, username: String) {
        replaceFragment(
            ChatListFragment.newInstance(sessionID, username),
            Consts.FRAGMENT_TAG_CHAT_LIST,
            false
        )
    }


    private fun setStartMessagingEnabled(enabled: Boolean) {
        val scale = if (enabled) 1f else 0f
        binding.appStartMessaging.animate().scaleY(scale).scaleX(scale).setDuration(300).start()
        binding.appStartMessaging.isEnabled = enabled
    }

    private fun setUIViewsEnabled(enabled: Boolean) {
        setStartMessagingEnabled(enabled)
        binding.buttonSignIn.isEnabled = enabled
        binding.textInputUsername.isEnabled = enabled
    }

    private fun showUIProgressIsLoading(isVisible: Boolean) {
        binding.mainProgressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun onLogInClick() {
        val inputUsernameText = binding.textInputUsername.text.toString()
        if (inputUsernameText.isBlank()) {
            Toast.makeText(context, getString(R.string.toast_create_username), Toast.LENGTH_SHORT).show()
            return
        }
        setUIViewsEnabled(false)
        showUIProgressIsLoading(true)
        vm.saveUsername(inputUsernameText)
        vm.requestSessionIDFromTCP()
    }
}