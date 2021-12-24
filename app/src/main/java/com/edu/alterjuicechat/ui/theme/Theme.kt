package com.edu.mynewcompose.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.alterjuicechat.R


// private val DarkColorPalette = darkColors(
//     primary = Purple200,
//     primaryVariant = Purple700,
//     secondary = Teal200
// )



private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariantColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryColorVariant,
    surface = surfaceColor,
    onSurface = onSurfaceColor,
    background = app_background,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


@Composable
fun getAppBarModifier(): Modifier{
    return Modifier
        .height(50.dp)
        .shadow(5.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.primary)
}

val AppBarTitleStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, textAlign = TextAlign.Center,color=white)

@Composable
fun GetAppBarFrame(modifyAppBarWithContent: (@Composable BoxScope.() -> Unit)?=null){
    Box(modifier = getAppBarModifier(), contentAlignment = Alignment.TopCenter) {
        modifyAppBarWithContent?.invoke(this)
    }
}

@Composable
fun GetAppBarBoxWithTitle(showTitle: Boolean=true, pageTitleText: String=stringResource(id = R.string.app_name),
                 modifyAppBarWithContent: (@Composable BoxScope.() -> Unit)?=null){
    GetAppBarFrame{
        if (showTitle) {
            Text(pageTitleText, style = AppBarTitleStyle,
                modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 16.dp))
        }
        modifyAppBarWithContent?.invoke(this)
    }
}


@Composable
fun TestComposableTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    // val colors = if (darkTheme) {
    //     DarkColorPalette
    // } else {
    //     LightColorPalette
    // }
    val colors = LightColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}