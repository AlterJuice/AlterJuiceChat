package com.edu.alterjuicechat

object Consts {
    const val BLANK_TITLE_PLACEHOLDER = "No title"
    const val BLANK_USERNAME_PLACEHOLDER = "Who is it?"
    const val PROFILE_PREFERENCES = "profile_preferences"
    const val PROFILE_KEY_NAME = "name"
    const val APP_PACKAGE = "com.edu.alterjuicechat"
    // const val ACTION_NOTIFICATION_CLICK = "NOTIFICATION_CLICKED"
    const val FRAGMENT_PARAM_SESSION_ID = "sessionID"
    const val FRAGMENT_PARAM_TCP_IP = "tcpIP"
    const val FRAGMENT_PARAM_USER_ID = "userID"
    const val FRAGMENT_PARAM_USER_NAME = "user_name"
    const val FRAGMENT_PARAM_CHAT_TITLE = "chat_title"

    // const val CHANNEL_ID = "channelId"
    // const val CHANNEL_NAME = "channelName"
    // const val CHANNEL_DESCRIPTION = "channelDescription"
    // const val NOTIFICATION_ID = 123123

    // Why emulators are not receiving packets over udp: use another ip
    // https://stackoverflow.com/a/41981731

    const val UDP_ADDRESS_FOR_EMULATOR = "10.0.2.2"
    const val UDP_ADDRESS_FOR_DEVICES = "255.255.255.255"
    val UDP_ADDRESS_BOTH = listOf(UDP_ADDRESS_FOR_EMULATOR, UDP_ADDRESS_FOR_DEVICES)
    const val UDP_ADDRESS = UDP_ADDRESS_FOR_DEVICES
    // TODO("Change UDP_ADDRESS to UDP_ADDRESS_FOR_EMULATOR before build APK for devices
    //  and vice verse - use UDP_ADDRESS_FOR_EMULATOR for emulator app launches")
    const val UDP_PORT = 8888
    const val TCP_PORT = 6666

    const val PING_DELAY = 2000L
    const val TCP_TIMEOUT = 5000
    const val UDP_TIMEOUT = 3000
    const val UDP_DELAY = 0L
    const val TCP_CONNECTING_DELAY = 1000L
    const val UDP_PACKET_SIZE = 256


}