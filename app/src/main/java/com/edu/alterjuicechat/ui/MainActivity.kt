package com.edu.alterjuicechat.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.socket.TCPWorker
import com.edu.alterjuicechat.ui.base.BaseActivity
import com.edu.mynewcompose.ui.theme.TestComposableTheme
import com.edu.mynewcompose.ui.theme.app_background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : BaseActivity() {
    private val tcpWorker: TCPWorker = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setContent{
        //     Surface(color=app_background,
        //     ) {
        //         Box(contentAlignment = Alignment.Center, modifier=Modifier.padding(vertical = 4.dp, horizontal = 8.dp)){
        //             SayHello(name = "Compose")
        //         }
        //     }
        // }
        replaceFragment(AuthFragment())
    }
    override fun onDestroy() {
        lifecycleScope.launch(Dispatchers.IO){
            tcpWorker.disconnect(1)
            tcpWorker.stopAll()
        }
        super.onDestroy()
    }
}



@Composable
fun SayHello(name: String){
    Text(text="Hello $name",)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestComposableTheme {
        SayHello("Android")
    }
}