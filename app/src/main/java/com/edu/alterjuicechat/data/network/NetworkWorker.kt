package com.edu.alterjuicechat.data.network

import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class NetworkWorker(private val ip: String, private val port: Int) : Thread() {

    val BaseDto.toJson: String
        get() = gsonManager.toJson(this)

    private lateinit var clientSocket: Socket
    private var ioOut: PrintWriter? = null
    private var ioIn: BufferedReader? = null

    private val gsonManager: Gson = Gson()

    private lateinit var job: Job

    private lateinit var jobPingPong: Job
    private val generator = GeneratorDto(gsonManager)

    private val flow: MutableSharedFlow<BaseDto> = MutableSharedFlow()
    private val channel = Channel<BaseDto>()
    val sharedFlow = flow.asSharedFlow()

    override fun run() {
        try {

            clientSocket = Socket(ip, port)
            clientSocket.keepAlive = true
            ioOut = PrintWriter(clientSocket.getOutputStream(), true)
            ioIn = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            jobPingPong = MainScope().launch {
                withContext(Dispatchers.IO){
                    while (true){
                        ioOut?.println(generator.generatePing("IdPing").toJson)
                        delay(2000)
                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }

//        jobPingPong.start()
        // job = MainScope().launch {
        //     withContext(Dispatchers.IO){
        //         flow.collect(){
        //             ioOut.println(objectToJson(it))
        //             // sendActionToServer(it)
        //         }
        //     }
        // }
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

    private fun sendToServer(action: BaseDto){
        ioOut?.println(generator.objectToJson(action))
    }

//    private fun sendActionToServer(baseDto: BaseDto) {
//        MainScope().launch {
//            withContext(Dispatchers.IO) {
//                ioOut.println(objectToJson(baseDto))
//            }
//         }
//    }



    override fun interrupt() {
        super.interrupt()
        ioIn?.close()
        ioOut?.close()
        jobPingPong.cancel()
        clientSocket.close()
    }
}