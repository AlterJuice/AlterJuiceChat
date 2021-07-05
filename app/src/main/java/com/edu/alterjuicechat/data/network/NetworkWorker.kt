package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.data.network.model.BaseDto
import com.edu.alterjuicechat.data.network.model.ConnectDto
import com.edu.alterjuicechat.data.network.model.Payload
import com.edu.alterjuicechat.data.network.model.SendMessageDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class NetworkWorker(val ip: String, val port: Int) : Thread() {

    private lateinit var clientSocket: Socket
    private lateinit var ioOut: PrintWriter
    private lateinit var ioIn: BufferedReader

    private val gsonManager: Gson = Gson()

    private lateinit var job: Job

    private val flow: MutableSharedFlow<BaseDto> = MutableSharedFlow()

    override fun run() {


    }

    private fun connect(id: String, name: String) {
        val payload = ConnectDto(id, name)
        val action = generateAction(BaseDto.Action.CONNECT, payload)
        // sendActionToServer(action)
        flow.tryEmit(action)
    }

    fun connectToServer() {

    }

    fun sendMessage(id: String, receiver: String, message: String) {
        val payload = SendMessageDto(id, receiver, message)
        val action = generateAction(BaseDto.Action.SEND_MESSAGE, payload)
        // flow.emit(action)
        sendActionToServer(action)
    }

    private fun generateAction(action: BaseDto.Action, payload: Payload): BaseDto {
        return BaseDto(action, objectToJson(payload))
    }

    private fun sendActionToServer(baseDto: BaseDto) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                ioOut.println(objectToJson(baseDto))
            }
        }
    }

    private fun objectToJson(obj: Any): String {
        return gsonManager.toJson(obj)
    }

    override fun start() {
        super.start()
        job = MainScope().launch {
            withContext(Dispatchers.IO){
                clientSocket = Socket(ip, port)
                ioOut = PrintWriter(clientSocket.getOutputStream(), true)
                ioIn = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                flow.collect(){
                    ioOut.println(objectToJson(it))
                    // sendActionToServer(it)
                }
            }
        }
        // job = MainScope().launch {
        //     withContext(Dispatchers.IO) {
        //         clientSocket = Socket(ip, port)
        //         ioOut = PrintWriter(clientSocket.getOutputStream(), true)
        //         ioIn = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        //     }
        // }


    }

    override fun interrupt() {
        super.interrupt()
        ioIn.close()
        ioOut.close()
        clientSocket.close()
    }
}