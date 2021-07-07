package com.edu.alterjuicechat.viewmodels

import androidx.lifecycle.ViewModel
import com.edu.alterjuicechat.data.network.NetworkWorker

class AuthViewModel(private val networkWorker: NetworkWorker) : ViewModel() {


    fun getTcpIp(): String{
        return networkWorker.getIpFromUdp()
    }


}