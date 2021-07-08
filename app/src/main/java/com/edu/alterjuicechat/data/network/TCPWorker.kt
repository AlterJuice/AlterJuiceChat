package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.edu.alterjuicechat.data.network.model.dto.ConnectedDto
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCPWorker(private val gson: Gson) {
    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader
    private val generator = GeneratorDto(gson)

    val BaseDto.toJson: String
        get() = gson.toJson(this)


    fun connectWithTcp(tcpIp: String, userName: String): String{
        var attempts = 0
        var sessionId: String? = null
        val sleepMillis = 1000
        while (sessionId == null){
            println("Trying to connect with TCP to ${tcpIp}:${Consts.TCP_PORT}; Sleep ${sleepMillis/1000}s; Attempt #${++attempts}")
            try {
                clientSocket = Socket(tcpIp, Consts.TCP_PORT)
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val line = reader.readLine()
                sessionId = gson.fromJson(gson.fromJson(line, BaseDto::class.java).payload, ConnectedDto::class.java).id
            }catch (e: Exception){
                e.printStackTrace()
                Thread.sleep(1000)
                continue
            }
        }
        println("TCP Connected! SessionID: $sessionId;")
        return connectWithIdAndUsername(sessionId, userName)
    }
    private fun connectWithIdAndUsername(sessionID: String, username: String): String{

        return sessionID
    }

    fun connect(id: String, name: String){
        val action = generator.generateConnect(id, name)
        sendToServer(action)
    }


    private fun sendToServer(action: BaseDto){
        writer.println(action.toJson)
    }
}