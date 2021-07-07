package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.edu.alterjuicechat.data.network.model.dto.ConnectedDto
import com.edu.alterjuicechat.data.network.model.dto.UdpDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.*


class NetworkWorker(private val gson: Gson) : Thread() {

    val BaseDto.toJson: String
        get() = gson.toJson(this)


    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader


    private lateinit var job: Job

    private var sharedTcpIp: String? = null

    private lateinit var jobPingPong: Job
    private val generator = GeneratorDto(gson)

    private val flow: MutableSharedFlow<BaseDto> = MutableSharedFlow()
    val sharedFlow = flow.asSharedFlow()
    private var ioIsClosed = true

    private var sessionId: String? = null



    override fun run() {
        try {
            updateSharedTcpIp()
            connectWithTcp()
            jobPingPong = MainScope().launch {
                withContext(Dispatchers.IO){
                    while (true){
                        if (canPerformRequests()){
                            sendToServer(generator.generatePing(getSessionId()))
                        }
                        delay(2000)
                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun updateSharedTcpIp(){
        sharedTcpIp = getIpFromUdp()
    }
    private fun updateSessionId(){

    }

    private fun canPerformRequests(): Boolean{
        return sessionId != null
    }


    private fun connectWithTcp(){
        var attempts = 0
        while (!tcpSocketIsReady()){
            println("Trying to connect with TCP to ${sharedTcpIp}:${Consts.TCP_PORT}; Attempt #${++attempts}")
            try {
                clientSocket = Socket(getSharedTcpIp(), Consts.TCP_PORT)
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val line = reader.readLine()
                sessionId = gson.fromJson(gson.fromJson(line, BaseDto::class.java).payload, ConnectedDto::class.java).id
                ioIsClosed = false
                println("TCP Connected! SessionID: $sessionId; Last line is: $line")
            }catch (e: Exception){
                e.printStackTrace()
                sleep(2000)
                continue
            }
        }
        sendToServer(generator.generateConnect(sessionId!!, "AlterJuice"))
    }

    private fun tcpSocketIsReady(): Boolean{
        return this::clientSocket.isInitialized
                && !clientSocket.isClosed
                && !ioIsClosed && sessionId != null
    }

    private fun getSessionId(): String{
        if (!tcpSocketIsReady()){
            connectWithTcp()
        }
        return sessionId!!
    }

    private fun getSharedTcpIp(): String{
        if (sharedTcpIp == null){
            updateSharedTcpIp()
        }
        return sharedTcpIp as String
    }

    private fun getSocket(): Socket{
        if (sharedTcpIp == null)
            updateSharedTcpIp()
        if (!tcpSocketIsReady())
            connectWithTcp()
        return clientSocket
    }

    private fun getWriter(): PrintWriter {
        if (!tcpSocketIsReady())
            connectWithTcp()
        return writer
    }

    private fun getReader(): BufferedReader? {
        if (!tcpSocketIsReady())
            connectWithTcp()
        return reader
    }

    private fun sendToServer(action: BaseDto){
        getWriter()?.println(action.toJson)
    }


    private fun getIpFromUdp(): String{
        // 10.0.2.2
        // 255.255.255.255
        var attempts = 0
        val messageToSend = "Send ip'des".toByteArray()
        var resultTcpIp: String? = null
        while (resultTcpIp == null){
            println("Trying to connect with UDP to ${Consts.UDP_ADDRESS}:${Consts.UDP_PORT}; Attempt #${++attempts}")
            val datagramSocket = DatagramSocket().apply { soTimeout = 5000 }
            val sendPacket = DatagramPacket(messageToSend, messageToSend.size,
                InetAddress.getByName(Consts.UDP_ADDRESS), Consts.UDP_PORT)
            val receiveBuffer = ByteArray(256)
            val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)

            try {
                datagramSocket.send(sendPacket)
                datagramSocket.receive(receivePacket)
                val str = String(receivePacket.data, 0, receivePacket.length)
                resultTcpIp = gson.fromJson(str, UdpDto::class.java).ip
            }catch (timeout: SocketTimeoutException){
                println("Datagram_Socket_timeout error")
                sleep(2000)
                continue
            }/*finally {
                datagramSocket.close()
                println("DatagramSocketIsClosed")
            }*/
        }
        return resultTcpIp
    }

    override fun interrupt() {
        disconnectFromTcp()
        jobPingPong.cancel()
        super.interrupt()
    }

    private fun disconnectFromTcp(){
        clientSocket.close()
        writer.close()
        reader.close()
        sharedTcpIp = null
        ioIsClosed = true
    }


    suspend fun connect(id: String, name: String){
        val action = generator.generateConnect(id, name)
        sendToServer(action)
        flow.emit(action)
    }

    suspend fun getUsers(id: String){
        val action = generator.generateGetUsers(id)
        sendToServer(action)
        flow.emit(action)
    }


    suspend fun sendMessage(id: String, receiver: String, message: String) {
        val action = generator.generateSendMessage(id, receiver, message)
        sendToServer(action)
        flow.emit(action)
    }


}