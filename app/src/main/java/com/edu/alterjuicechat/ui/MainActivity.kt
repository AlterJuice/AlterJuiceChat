package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.edu.alterjuicechat.EchoServer
import com.edu.alterjuicechat.R
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class MainActivity : AppCompatActivity() {
    private lateinit var t2: EchoServer

    lateinit var t: Thread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // startService(Intent(this, TCPService::class.java))
        replaceFragment(AuthFragment.newInstance())
        t2 = EchoServer()

        t = Thread {
            Log.d("T thread", "opening")
            val datagramSocket = DatagramSocket(8888)
            val buf = ByteArray(256)
            val packet = DatagramPacket(buf, buf.size, InetAddress.getByName("255.255.255.255"), 6666)
            datagramSocket.receive(packet)
            Log.d("MainActivity", "____"+datagramSocket.localAddress.toString())
            Thread.sleep(3000)
        }
        t.start()



//        datagramSocket.receive(DatagramPacket())
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

    override fun onDestroy() {
        super.onDestroy()
        t2.interrupt()

    }

}