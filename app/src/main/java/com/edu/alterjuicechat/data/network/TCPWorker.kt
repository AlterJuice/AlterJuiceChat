package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.Consts
import com.edu.alterjuicechat.data.network.model.dto.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
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
    private var eventCollectorJob: Job? = null
    private var pingPongJob: Job? = null

    private lateinit var tcpIp: String


    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)
    private val eventFlow = MutableSharedFlow<String>()



    suspend fun connectWithTcp(){
        val tcpIp: String = getTcpIP()
        var attempts = 0
        while (true){  // was while sessionID == null
            println("Trying to connect with TCP to ${tcpIp}:${Consts.TCP_PORT}; Sleep ${Consts.TCP_CONNECTING_DELAY/1000}s; Attempt #${++attempts}")
            try {
                clientSocket = Socket(tcpIp, Consts.TCP_PORT).apply { soTimeout = Consts.TCP_TIMEOUT }
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                launchEventCollector()
                runUpdatesReceiver()
                return
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
                try {
                    sendToServer(generator.generatePing(sessionID))
                }catch (e: Exception){
                    e.printStackTrace()
                }
                delay(Consts.PING_DELAY)
            }
        }
    }



    private suspend fun runUpdatesReceiver(){
        receivingJob = scope.launch(Dispatchers.IO) {
            while(true) {
                val line = reader.readLine()
                eventFlow.emit(line)
            }
        }
    }

    private fun getSessionID(): String{
        return dataStore.mutableSessionID.value!!
    }
    private fun getUsername(): String{
        return dataStore.mutableUsername.value!!
    }

    private fun getTcpIP(): String{
        return dataStore.mutableTcpIP.value!!
    }



    suspend fun launchEventCollector(){
        eventCollectorJob = scope.launch(Dispatchers.IO) {
            eventFlow.collect{
                val baseDto = parser.parseBaseDto(it)
                println("EventListener: Action ${baseDto.action}; Payload: ${baseDto.payload}")
                when (baseDto.action) {
                    BaseDto.Action.USERS_RECEIVED -> {
                        dataStore.processRawUsers(parser.parseUsersList(baseDto.payload))
                    }
                    BaseDto.Action.PONG -> {}
                    // BaseDto.Action.CONNECT -> TODO()
                    BaseDto.Action.CONNECTED -> {
                        val sessionID = parser.parseConnected(baseDto.payload).id
                        withContext(Dispatchers.Main) {
                            dataStore.mutableSessionID.value = sessionID
                            println("TCP Connected! SessionID: ${getSessionID()};")
                        }
                        launchPingPong(sessionID)
                        connect(sessionID, getUsername())
                    }
                    // BaseDto.Action.SEND_MESSAGE -> TODO()
                    BaseDto.Action.NEW_MESSAGE -> {
                        withContext(Dispatchers.Main) {
                            dataStore.processNewMessage(parser.parseMessage(baseDto.payload))
                        }
                    }
                    // BaseDto.Action.DISCONNECT -> TODO()
                }
            }
        }
    }

    private fun stopReceiver(){
        receivingJob?.cancel()
    }

    private fun stopEventListener(){
        eventCollectorJob?.cancel()
    }

    private fun stopAll(){
        stopReceiver()
        stopEventListener()
    }

    private suspend fun connectWithIdAndUsername(sessionID: String, username: String): String{
        connect(sessionID, username)
        return sessionID
    }

    suspend fun requestUsers() {
        val action = generator.generateGetUsers(getSessionID())
        sendToServer(action)
    }

    suspend fun connect(id: String, name: String){
        val action = generator.generateConnect(id, name)
        sendToServer(action)
    }
    suspend fun disconnect(id: String, code: Int){
        val action = generator.generateDisconnect(id, code)
        sendToServer(action)
    }

    suspend fun sendMessage(id: String, receiverID: String, message: String){
        val action = generator.generateSendMessage(id, receiverID, message)
        sendToServer(action)
        dataStore.processSendMessage(id, receiverID, message)
    }

    private suspend fun sendToServer(action: BaseDto){
        writer.println(action.toJson(gson))
    }
}