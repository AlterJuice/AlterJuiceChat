package com.edu.alterjuicechat

object Consts {
    const val BLANK_TITLE_PLACEHOLDER = "No title"
    const val PROFILE_PREFERENCES = "profile_preferences"
    const val PROFILE_KEY_NAME = "name"
    const val FRAGMENT_PARAM_SESSION_ID = "sessionID"
    const val FRAGMENT_PARAM_USER_ID = "userID"
    const val FRAGMENT_PARAM_USER_NAME = "user_name"
    const val FRAGMENT_PARAM_CHAT_TITLE = "chat_title"

    const val FRAGMENT_TAG_PRIVATE_CHAT = "Tag:PrivateChat"
    const val FRAGMENT_TAG_CHAT_LIST = "Tag:ChatList"

    // Why emulators are not receiving packets over udp: use another ip
    // https://stackoverflow.com/a/41981731
    const val SIMPLE_MESSAGE_DATE_FORMAT = "k:mm"
    private const val UDP_ADDRESS_FOR_EMULATOR = "10.0.2.2"
    private const val UDP_ADDRESS_FOR_DEVICES = "255.255.255.255"
    const val UDP_ADDRESS = UDP_ADDRESS_FOR_DEVICES
    val UDP_ADDRESS_BOTH = listOf(UDP_ADDRESS_FOR_EMULATOR, UDP_ADDRESS_FOR_DEVICES)
    const val UDP_PORT = 8888
    const val TCP_PORT = 6666

    const val PING_DELAY = 2000L
    const val TCP_TIMEOUT = 5000
    const val UDP_TIMEOUT = 3000
    const val UDP_DELAY = 0L
    const val TCP_CONNECTING_DELAY = 1000L
    const val UDP_PACKET_SIZE = 256
    const val USERS_LOOP_UPDATER_DELAY = 5000L


}