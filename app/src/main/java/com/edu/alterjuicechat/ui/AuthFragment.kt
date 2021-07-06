package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.NetworkWorker
import com.edu.alterjuicechat.repo.interfaces.AuthRepo
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named


class AuthFragment : Fragment() {

    private val authRepo: AuthRepo = get()
    private val viewModel by sharedViewModel<ChatViewModel>(named("1"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authRepo.userIsLoggedIn()){
            (requireActivity() as MainActivity).replaceFragment(ChatsListFragment.newInstance())
            onDestroy()
        }

        viewModel.sendMessage("RECEIVER VM", "MESSAGEE")
        viewModel.getUsers()

        // worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
    }

    override fun onDestroy() {
        super.onDestroy()
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