package com.edu.alterjuicechat.domain

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
    const val USERS_LOOP_UPDATER_DELAY = 5000L

}