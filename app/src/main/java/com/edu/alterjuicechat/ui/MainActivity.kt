package com.edu.alterjuicechat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.TCPWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
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
        MainScope().launch(Dispatchers.IO){
            tcpWorker.disconnect(1)
            tcpWorker.stopAll()
        }
        super.onDestroy()
    }

}