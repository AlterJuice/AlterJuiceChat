package com.edu.alterjuicechat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.edu.alterjuicechat.ui.MainActivity

class TCPBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.endsWith(Consts.ACTION_NOTIFICATION_CLICK) == true){
            processNotificationClickAction(context)
        }
    }

    fun processNotificationClickAction(context: Context){
        context.startActivity(Intent(context, MainActivity::class.java).apply{
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}