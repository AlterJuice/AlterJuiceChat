package com.edu.alterjuicechat

import android.util.Log
import com.edu.alterjuicechat.data.network.model.dto.BaseDto
import com.edu.alterjuicechat.data.network.model.dto.ConnectDto
import com.google.gson.Gson
import java.io.*

class TCPHandler(private val dataInputStream: BufferedReader, private val dataOutputStream: PrintWriter) : Thread() {
    private var streamsAreOpened = true
    override fun run() {
        while (true) {
            try {
                Log.d("TCPHandler", "XXX_Received: " + dataInputStream.readLine())
                dataOutputStream.println(
                    Gson().toJson(BaseDto(BaseDto.Action.CONNECT, Gson().toJson(ConnectDto("123", "Dave")))))
                dataOutputStream.flush()

                sleep(2000L)
                dataOutputStream.println()
            } catch (e: IOException) {
                e.printStackTrace()
                tryCloseInputs()
            } catch (e: InterruptedException) {
                e.printStackTrace()
                tryCloseInputs()
            }
        }
    }
    private fun tryCloseInputs(){
        try {
            closeInputs()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    private fun closeInputs(){
        streamsAreOpened = false
        dataInputStream.close()
        dataOutputStream.close()
    }
}