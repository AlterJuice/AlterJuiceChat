package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.*
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCPWorker(private val gson: Gson) {
    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader
    private val generator = GeneratorDto(gson)
    private var receivingJob: Job? = null

    val BaseDto.toJson: String
        get() = gson.toJson(this)


    suspend fun connectWithTcp(tcpIp: String, userName: String): String{
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
//                Thread.sleep(1000)
                continue
            }
        }
        println("TCP Connected! SessionID: $sessionId;")
        return connectWithIdAndUsername(sessionId, userName)
    }

    val eventFlow = MutableSharedFlow<BaseDto>()
    // val eventFlow = MutableSharedFlow<Payload>()

    private suspend fun runUpdatesReceiver(){
        receivingJob = MainScope().launch {
            while(true) {
                val line = reader.readLine()
                val response = gson.fromJson(line, BaseDto::class.java)
                eventFlow.emit(response)

            }
        }
    }

    private fun launchEventFlows(){
        MainScope().launch {
            eventFlow.collect{
                when (it.action){
                    BaseDto.Action.USERS_RECEIVED -> {}
                    BaseDto.Action.PING -> TODO()
                    BaseDto.Action.PONG -> TODO()
                    BaseDto.Action.CONNECT -> TODO()
                    BaseDto.Action.CONNECTED -> TODO()
                    BaseDto.Action.GET_USERS -> TODO()
                    BaseDto.Action.SEND_MESSAGE -> TODO()
                    BaseDto.Action.NEW_MESSAGE -> TODO()
                    BaseDto.Action.DISCONNECT -> TODO()
                }
            }
        }
    }

    private fun stopReceiver(){
        receivingJob?.cancel()

    }

    private fun connectWithIdAndUsername(sessionID: String, username: String): String{
        return sessionID
    }

    suspend fun getUsers(id: String): UsersReceivedDto{
        val action = generator.generateGetUsers(id)
        sendToServer(action)
        val line = reader.readLine()
        println(line)
        return gson.fromJson(gson.fromJson(line, BaseDto::class.java).payload, UsersReceivedDto::class.java)
    }

    suspend fun connect(id: String, name: String){
        val action = generator.generateConnect(id, name)
        sendToServer(action)
    }
    suspend fun disconnect(id: String, code: Int){
        val action = generator.generateDisconnect(id, code)
        sendToServer(action)
    }

    suspend fun sendMessage(id: String, receiverID: String, message: String): MessageDto{
        val action = generator.generateSendMessage(id, receiverID, message)
        sendToServer(action)
        return MessageDto(UserDto(id, "ME"), message)
    }

    private suspend fun sendToServer(action: BaseDto){
        writer.println(action.toJson)
    }
}