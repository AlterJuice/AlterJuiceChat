package com.edu.alterjuicechat.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.TCPWorker
import com.edu.alterjuicechat.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class MainActivity : BaseActivity() {
    private val tcpWorker: TCPWorker = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(AuthFragment())
    }

    override fun onDestroy() {
        lifecycleScope.launch(Dispatchers.IO){
            tcpWorker.disconnect(1)
            tcpWorker.stopAll()
        }
        super.onDestroy()
    }

}