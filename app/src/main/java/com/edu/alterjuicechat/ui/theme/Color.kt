package com.edu.mynewcompose.ui.theme
import android.graphics.Color as androidColor
import androidx.compose.ui.graphics.Color as ComposeColor

val Purple200 = ComposeColor(0xFFBB86FC)
val Purple500 = ComposeColor(0xFF6200EE)
val Purple700 = ComposeColor(0xFF3700B3)
val Teal200 = ComposeColor(0xFF03DAC5)

fun ComposeColor.Companion.from(color: String) = ComposeColor(androidColor.parseColor(color))


val black = ComposeColor.from("#FF000000")
val white = ComposeColor.from("#FFFFFFFF")
val app_background = ComposeColor.from("#B3E5FC")
val chat_item_background = ComposeColor.from("#90CAF9")
val primaryDark = ComposeColor.from("#1985c1")
val red = ComposeColor.from("#E53935")
val message_item_mine_background_color = ComposeColor.from("#60b4f4")
val message_item_not_mine_background_color = ComposeColor.from("#AED581")


val primaryColor = ComposeColor.from("#60b4f4")
val primaryVariantColor = ComposeColor.from("#97e6ff")
val secondaryColor = ComposeColor.from("#2196f3")
val secondaryColorVariant = ComposeColor.from("#6ec6ff")
val surfaceColor = white
val onSurfaceColor = black