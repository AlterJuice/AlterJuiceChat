package com.edu.alterjuicechat

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import com.edu.alterjuicechat.data.network.TCPWorker
import org.koin.android.ext.android.get
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean

class TCPService : Service() {
    // OnCreate and OnStartCommand: https://stackoverflow.com/a/14182844
    // Using AtomicBool: https://stackoverflow.com/a/4501314
    private val tcpWorker: TCPWorker = get()
    private val importance = NotificationManager.IMPORTANCE_HIGH


    private var serverSocket: ServerSocket? = null

    private val isWorking = AtomicBoolean(true)

    override fun onCreate() {
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Runs once per startService called
        createNotification()
        return START_STICKY
    }

    private fun createNotification(){
        val channel = NotificationChannel(Consts.CHANNEL_ID, Consts.CHANNEL_NAME, importance).apply {
            description = Consts.CHANNEL_DESCRIPTION
            lightColor = Color.BLUE
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        startForeground(Consts.NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        return Notification.Builder(this, Consts.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("TCP notification")
            .setContentText("TCP TEXT")
            .setAutoCancel(true)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(PendingIntent.getBroadcast(this, 0, getBroadcastIntent(), PendingIntent.FLAG_UPDATE_CURRENT))
            .build()
    }

    private fun getBroadcastIntent(): Intent {
        return Intent("${Consts.APP_PACKAGE}.${Consts.ACTION_NOTIFICATION_CLICK}").apply {
            setPackage(Consts.APP_PACKAGE)
        }
    }


    override fun onDestroy() {
        isWorking.set(false)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}