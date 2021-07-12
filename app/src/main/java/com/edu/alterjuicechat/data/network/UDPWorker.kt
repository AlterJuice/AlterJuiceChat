package com.edu.alterjuicechat.data.network

import android.util.Log
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.UdpDto
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.*

class UDPWorker(private val gson: Gson, private val dataStore: DataStore) {


    suspend fun requestTcpIP(){
        // 10.0.2.2 for emulator
        // 255.255.255.255 for device
        var attempts = 0
        val messageToSend = "Send ip'des (${Date()})".toByteArray()
        var resultTcpIp: String? = null
        val datagramSocket = DatagramSocket().apply { soTimeout = Consts.UDP_TIMEOUT }
        val sendPacket = DatagramPacket(messageToSend, messageToSend.size,
            InetAddress.getByName(Consts.UDP_ADDRESS), Consts.UDP_PORT)
        val receiveBuffer = ByteArray(Consts.UDP_PACKET_SIZE)
        val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)

        while (resultTcpIp == null){
            Log.i("UDPWorker@requestTcpIP","Trying to connect with UDP to ${Consts.UDP_ADDRESS}:${Consts.UDP_PORT}; Attempt #${++attempts}")
            try {
                datagramSocket.send(sendPacket)
                datagramSocket.receive(receivePacket)
                val str = String(receivePacket.data, 0, receivePacket.length)
                resultTcpIp = gson.fromJson(str, UdpDto::class.java).ip
                dataStore.mutableTcpIP.postValue(resultTcpIp)
                Log.i("UDPWorker@requestTcpIP","UDP Connected! TCP IP: $resultTcpIp;")
            }catch (timeout: SocketTimeoutException){
                delay(Consts.UDP_DELAY)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}