package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.GeneratorDto
import com.edu.alterjuicechat.socket.dto.ParserDto
import com.edu.alterjuicechat.socket.dto.entities.BaseDto
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketTimeoutException

const val TCP_PORT = 6666
const val TCP_TIMEOUT = 5000
const val TCP_CONNECTING_DELAY = 1000L
const val TCP_PING_DELAY = 4000L

internal class TCPWorkerImpl(private val parser: ParserDto, private val generator: GeneratorDto, private val dataStore: DataStore): TCPWorker {
    private lateinit var clientSocket: Socket
    private lateinit var writer: PrintWriter
    private lateinit var reader: BufferedReader

    private var receivingJob: Job? = null
    private var pingPongJob: Job? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)


    private fun getSessionID(): String{
        return dataStore.getSessionIDValue()
    }

    private fun getUsername(): String{
        return dataStore.getUsernameValue()
    }

    private fun getTcpIP(): String{
        return dataStore.getTcpIpValue()
    }

    override suspend fun requestSessionID() {
        val tcpIp: String = getTcpIP()
        var attempts = 0
        while (true){
            println("Trying to connect with TCP to ${tcpIp}:$TCP_PORT; Sleep ${TCP_CONNECTING_DELAY /1000}s; Attempt #${++attempts}")
            try {
                clientSocket = Socket(tcpIp, TCP_PORT).apply { soTimeout = TCP_TIMEOUT }
                writer = PrintWriter(clientSocket.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                runUpdatesReceiver()
                break
            }catch (e: SocketTimeoutException){
                delay(TCP_CONNECTING_DELAY)
            }catch (e: Exception){
                e.printStackTrace()
                delay(TCP_CONNECTING_DELAY)
            }
        }
    }

    private suspend fun launchPingPong(sessionID: String){
        pingPongJob = scope.launch(Dispatchers.IO) {
            while (true){
                sendToServer(generator.generatePing(sessionID))
                delay(TCP_PING_DELAY)
            }
        }
    }

    private suspend fun runUpdatesReceiver(){
        receivingJob = scope.launch(Dispatchers.IO) {
            while(true) {
                val line = reader.readLine()
                handleUpdate(convertUpdateToBaseDto(line))
            }
        }
    }

    override suspend fun requestUsers() {
        val action = generator.generateGetUsers(getSessionID())
        sendToServer(action)
    }

    override suspend fun connect() {
        val sessionID = getSessionID()
        val username = getUsername()
        val action = generator.generateConnect(sessionID, username)
        sendToServer(action)
    }

    override suspend fun disconnect(code: Int) {
        val sessionID = getSessionID()
        val action = generator.generateDisconnect(sessionID, code)
        sendToServer(action)
    }
    override suspend fun sendMessage(chatID: String, message: String) {
        val sessionID = getSessionID()
        val action = generator.generateSendMessage(sessionID, chatID, message)
        sendToServer(action)
        withContext(Dispatchers.Main){
            dataStore.handleSendMessage(sessionID, chatID, message)
        }
    }

    override suspend fun sendToServer(action: BaseDto) {
        try {
            writer.println(generator.toJson(action))
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    override fun convertUpdateToBaseDto(stringUpdate: String): BaseDto {
        return parser.parseBaseDto(stringUpdate)
    }


    override suspend fun handleUpdate(baseDto: BaseDto) {

        when (baseDto.action) {
            // BaseDto.Action.PONG -> {}
            BaseDto.Action.USERS_RECEIVED -> {
                withContext(Dispatchers.Main){
                    dataStore.handleNewUsers(parser.parseUsersList(baseDto.payload))
                }
            }
            BaseDto.Action.CONNECTED -> {
                val sessionID = parser.parseConnected(baseDto.payload).id
                withContext(Dispatchers.Main) {
                    dataStore.setSessionID(sessionID)
                    println("TCP Connected! SessionID: ${getSessionID()};")
                }
                connect()
                launchPingPong(sessionID)
            }
            BaseDto.Action.NEW_MESSAGE -> {
                withContext(Dispatchers.Main) {
                    dataStore.handleReceiveMessage(parser.parseMessage(baseDto.payload))
                }
            }
            else -> {
                // Other actions are not receivable
            }
        }
    }


    override suspend fun stopAll() {
        receivingJob?.cancel()
        pingPongJob?.cancel()
        reader.close()
        writer.close()
    }
}