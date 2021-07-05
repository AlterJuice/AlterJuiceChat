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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class AuthFragment : Fragment() {

    private val authRepo: AuthRepo = get()

    private lateinit var worker: NetworkWorker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authRepo.userIsLoggedIn()){
            (requireActivity() as MainActivity).replaceFragment(ChatsListFragment.newInstance())
            onDestroy()
        }

        worker = NetworkWorker("192.168.88.75", 6666)

        worker.connectToServer()
        worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
    }

    override fun onDestroy() {
        super.onDestroy()
        worker.interrupt()
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
            worker.sendMessage("ID", "RECEIVER", "MESSAGEEE")
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