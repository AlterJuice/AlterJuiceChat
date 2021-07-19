package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.ParserDto
import kotlinx.coroutines.delay
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.util.*

const val UDP_DELAY = 0L
const val UDP_PORT = 8888
const val UDP_TIMEOUT = 5000
const val UDP_PACKET_SIZE = 256
const val UDP_ADDRESS_FOR_EMULATOR = "10.0.2.2"
const val UDP_ADDRESS_FOR_DEVICES = "255.255.255.255"
const val UDP_ADDRESS = UDP_ADDRESS_FOR_DEVICES
val UDP_ADDRESS_BOTH = listOf(UDP_ADDRESS_FOR_EMULATOR, UDP_ADDRESS_FOR_DEVICES)

class UDPWorker(private val parser: ParserDto, private val dataStore: DataStore) {

    suspend fun requestTcpIP(){
        var attempts = 0
        val messageToSend = "Send ip'des (${Date()})".toByteArray()
        var resultTcpIp: String? = null
        val datagramSocket = DatagramSocket().apply { soTimeout = UDP_TIMEOUT}
        val receiveBuffer = ByteArray(UDP_PACKET_SIZE)
        val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
        var udpAddress: String
        while (resultTcpIp == null){
            udpAddress = UDP_ADDRESS_BOTH[attempts % 2]
            println("Trying to connect with UDP to ${udpAddress}:${UDP_PORT}; Attempt #${++attempts}")
            try {
                val sendPacket = DatagramPacket(messageToSend, messageToSend.size, InetAddress.getByName(udpAddress), UDP_PORT)
                datagramSocket.send(sendPacket)
                datagramSocket.receive(receivePacket)
                val udpPayloadStr = String(receivePacket.data, 0, receivePacket.length)
                resultTcpIp = parser.parseUdp(udpPayloadStr).ip
                dataStore.setTcpIp(resultTcpIp)
                println("UDP Connected! TCP IP: $resultTcpIp;")
            }catch (timeout: SocketTimeoutException){
                delay(UDP_DELAY)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}