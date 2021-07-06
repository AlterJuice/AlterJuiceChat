package com.edu.alterjuicechat

import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket


class EchoServer : Thread() {
    private val socket: DatagramSocket
    private var running = false
    private val buf = ByteArray(256)
    override fun run() {
        running = true
        while (running) {
            var packet = DatagramPacket(buf, buf.size)
            socket.receive(packet)
            val address = packet.address
            val port = packet.port
            packet = DatagramPacket(buf, buf.size, address, port)
            val received = String(packet.data, 0, packet.length)
            if (received == "end") {
                running = false
                continue
            }
            socket.send(packet)
        }
        socket.close()
    }

    init {
        socket = DatagramSocket(4445)
    }
}