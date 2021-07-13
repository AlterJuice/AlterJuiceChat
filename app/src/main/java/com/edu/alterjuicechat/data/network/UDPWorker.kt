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
        val receiveBuffer = ByteArray(Consts.UDP_PACKET_SIZE)
        val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
        var udpAddress = Consts.UDP_ADDRESS
        while (resultTcpIp == null){
            udpAddress = Consts.UDP_ADDRESS_BOTH[attempts % 2]
            Log.i("UDPWorker@requestTcpIP","Trying to connect with UDP to ${udpAddress}:${Consts.UDP_PORT}; Attempt #${++attempts}")
            try {
                val sendPacket = DatagramPacket(messageToSend, messageToSend.size,
                    InetAddress.getByName(udpAddress), Consts.UDP_PORT)
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