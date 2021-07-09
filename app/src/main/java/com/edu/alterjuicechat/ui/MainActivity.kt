package com.edu.alterjuicechat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.data.network.TCPWorker
import org.koin.android.ext.android.get


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // startService(Intent(this, TCPService::class.java))
        replaceFragment(AuthFragment())

    }

    fun replaceFragment(fragment: Fragment, tag: String = "", addToBackStack: Boolean = false){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment, tag)
            .apply {
                if (addToBackStack)
                    addToBackStack(tag)
            }
            .commit()
    }
}