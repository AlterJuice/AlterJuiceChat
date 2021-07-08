package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.UdpDto
import com.google.gson.Gson
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

class UDPWorker(private val gson: Gson) {


    fun getTcpIp(): String{
        // 10.0.2.2
        // 255.255.255.255
        var attempts = 0
        val messageToSend = "Send ip'des".toByteArray()
        var resultTcpIp: String? = null
        val datagramSocket = DatagramSocket().apply { soTimeout = 5000 }
        val sendPacket = DatagramPacket(messageToSend, messageToSend.size,
            InetAddress.getByName(Consts.UDP_ADDRESS), Consts.UDP_PORT)
        val receiveBuffer = ByteArray(256)
        val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
        while (resultTcpIp == null){
            println("Trying to connect with UDP to ${Consts.UDP_ADDRESS}:${Consts.UDP_PORT}; Attempt #${++attempts}")
            try {
                datagramSocket.send(sendPacket)
                datagramSocket.receive(receivePacket)
                val str = String(receivePacket.data, 0, receivePacket.length)
                resultTcpIp = gson.fromJson(str, UdpDto::class.java).ip
            }catch (timeout: SocketTimeoutException){
                println("Datagram_Socket_timeout error")
                Thread.sleep(2000)
                // continue
            }
        }
        return resultTcpIp
    }
}