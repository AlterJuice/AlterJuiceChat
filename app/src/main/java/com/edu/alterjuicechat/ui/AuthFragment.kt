package com.edu.alterjuicechat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val vm by viewModel<AuthViewModel>(named(Consts.VIEW_MODEL_NAME_AUTH))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        binding.appStartMessaging.setOnClickListener(::onConnectClick)
        return binding.root
    }

    private fun onConnectClick(v: View){
        Toast.makeText(context, getString(R.string.toast_searching_the_server), Toast.LENGTH_LONG).show()
        showUIProgressIsLoading(true)
        setStartMessagingEnabled(false)
        vm.flowGetTCPIpFromUDP {
            showUIProgressIsLoading(false)
            showTCPInfoText(it)
            // setStartMessagingEnabled(true)
            processNextStepWithIp(tcpIp = it)
        }
    }

    private fun showTCPInfoText(tcpIp: String){
        with(binding.foundTcpIpText){
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
        vm.saveUsername(inputUsernameText)
        showUIProgressIsLoading(true)
        vm.flowGetSessionIDFromTCP(tcpIp, inputUsernameText) {
            showUIProgressIsLoading(false)
            with(requireActivity() as MainActivity) {
                replaceFragment(ChatsListFragment.newInstance(it, inputUsernameText), "Chat", false)
            }
            vm.connect(it, inputUsernameText)
            setUIViewsEnabled(true)
        }
    }
}