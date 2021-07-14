package com.edu.alterjuicechat.data.network

import android.util.Log
import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.dto.GeneratorDto
import com.edu.alterjuicechat.data.network.dto.ParserDto
import com.edu.alterjuicechat.data.network.dto.model.BaseDto
import com.edu.alterjuicechat.repo.DataStore
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketTimeoutException

class TCPWorker(private val gson: Gson, private val dataStore: DataStore) {
    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader

    private val generator = GeneratorDto(gson)
    private val parser = ParserDto(gson)

    private var receivingJob: Job? = null
    private var pingPongJob: Job? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)


    suspend fun connectWithTcp(){
        val tcpIp: String = getTcpIP()
        var attempts = 0
        while (true){
            println("Trying to connect with TCP to ${tcpIp}:${Consts.TCP_PORT}; Sleep ${Consts.TCP_CONNECTING_DELAY/1000}s; Attempt #${++attempts}")
            try {
                clientSocket = Socket(tcpIp, Consts.TCP_PORT).apply { soTimeout = Consts.TCP_TIMEOUT }
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                runUpdatesReceiver()
                break
            }catch (e: SocketTimeoutException){
                delay(Consts.TCP_CONNECTING_DELAY)
            }catch (e: Exception){
                e.printStackTrace()
                delay(Consts.TCP_CONNECTING_DELAY)
            }
        }
    }

    private suspend fun launchPingPong(sessionID: String){
        pingPongJob = scope.launch(Dispatchers.IO) {
            while (true){
                sendToServer(generator.generatePing(sessionID))
                delay(Consts.PING_DELAY)
            }
        }
    }

    private suspend fun runUpdatesReceiver(){
        receivingJob = scope.launch(Dispatchers.IO) {
            while(true) {
                val line = reader.readLine()
                processUpdates(line)
            }
        }
    }

    private fun getSessionID(): String{
        return dataStore.mutableSessionID.value as String
    }

    private fun getUsername(): String{
        return dataStore.mutableUsername.value as String
    }

    private fun getTcpIP(): String{
        return dataStore.mutableTcpIP.value as String
    }


    private suspend fun processUpdates(stringUpdate: String){
        val baseDto = parser.parseBaseDto(stringUpdate)
        Log.i("EventListener", "Action ${baseDto.action}; Payload: ${baseDto.payload}")
        when (baseDto.action) {
            BaseDto.Action.USERS_RECEIVED -> { dataStore.processRawUsers(parser.parseUsersList(baseDto.payload)) }
            BaseDto.Action.PONG -> {}
            BaseDto.Action.CONNECTED -> {
                val sessionID = parser.parseConnected(baseDto.payload).id
                withContext(Dispatchers.Main) {
                    dataStore.mutableSessionID.value = sessionID
                    println("TCP Connected! SessionID: ${getSessionID()};")
                }
                connect(sessionID, getUsername())
                launchPingPong(sessionID)
            }
            BaseDto.Action.NEW_MESSAGE -> {
                withContext(Dispatchers.Main) {
                    dataStore.processNewMessage(parser.parseMessage(baseDto.payload))
                }
            }
        }
    }

    fun stopAll(){
        receivingJob?.cancel()
        pingPongJob?.cancel()
        reader.close()
        writer.close()
    }

    suspend fun requestUsers() {
        val action = generator.generateGetUsers(getSessionID())
        sendToServer(action)
    }

    suspend fun connect(){
        val sessionID = getSessionID()
        val name = getUsername()
        connect(sessionID, name)
    }

    private suspend fun connect(sessionID: String, username: String){
        val action = generator.generateConnect(sessionID, username)
        sendToServer(action)
    }

    suspend fun disconnect(code: Int){
        val sessionID = getSessionID()
        val action = generator.generateDisconnect(sessionID, code)
        sendToServer(action)
    }

    suspend fun sendMessage(id: String, receiverID: String, message: String){
        val action = generator.generateSendMessage(id, receiverID, message)
        sendToServer(action)
        dataStore.processSendMessage(id, receiverID, message)
    }

    private suspend fun sendToServer(action: BaseDto){
        try {
            writer.println(action.toJson(gson))
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
}