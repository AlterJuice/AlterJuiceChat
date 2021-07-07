package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.repo.interfaces.AuthRepo
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named


class AuthFragment : Fragment() {

    private val authRepo: AuthRepo = get()
    private val viewModel by sharedViewModel<AuthViewModel>(named("1"))
    private lateinit var binding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authRepo.userIsLoggedIn()) {
            (requireActivity() as MainActivity).replaceFragment(ChatsListFragment.newInstance())
            onDestroy()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSendMessage.setOnClickListener {
            // worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
        }
        binding.buttonOpenChatTest.setOnClickListener {
            with(requireActivity() as MainActivity) {
                replaceFragment(ChatFragment.newInstance("UserID", "UserName"), "Chat", true)
            }
        }

        binding.buttonConnect.setOnClickListener {
            val button: View = it
            button.isEnabled = false
            MainScope().launch {
                flow {
                    emit(viewModel.getTcpIp())
                }.flowOn(Dispatchers.IO)
                    .collect {
                        withContext(Dispatchers.Main) {
                            (button as Button).text = "Go next: $it"
                            button.isEnabled = true
                        }
                }
            }
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