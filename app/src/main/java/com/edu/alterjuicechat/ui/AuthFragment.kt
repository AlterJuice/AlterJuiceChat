package com.edu.alterjuicechat.ui

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentAuthBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import com.edu.mynewcompose.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StateHolder(){
    val formIsVisible: MutableState<Boolean> = mutableStateOf(false)
}

class MyAppState(
    val scaffoldState: ScaffoldState,
    private val resources: Resources,
) {
    val authFormVisibility = mutableStateOf(false)
    val progressVisibility = mutableStateOf(false)
    val username = mutableStateOf("")
    fun onStartMessagingClick(){
        authFormVisibility.value = true
        progressVisibility.value = true
    }
}

@Composable
fun rememberMyAppState(scaffoldState: ScaffoldState = rememberScaffoldState(), resources: Resources = LocalContext.current.resources) : MyAppState{
    return remember(scaffoldState, resources, /* ... */) {
        MyAppState(scaffoldState, resources, /* ... */)
    }
}



class AuthFragment : BaseFragment() {
    private lateinit var binding: FragmentAuthBinding
    private val vm by viewModel<AuthViewModel>()
    private val stateHolder = StateHolder()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        // return binding.root
        return ComposeView(requireContext()).apply {
           setContent {
               InitContent()
           }
        }
    }
    

    @Composable
    fun InitContent(){
            
            Surface() {
                val appState = rememberMyAppState()
                //Image(painter = painterResource(id = R.drawable.animated_background_gradient), contentDescription = "Background")
                Box(modifier= Modifier
                    .background(Brush.linearGradient(listOf(chat_item_background, white)))
                    .padding(10.dp)
                    .fillMaxSize()) {
                    if (appState.progressVisibility.value){
                        CircularProgressIndicator(modifier = Modifier.size(25.dp), color=primaryColor)
                    }
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)) {

                        Text(text = "AlterJuice Chat", color = ComposeColor.from("#01579B"), fontSize = 35.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally))
                        if (!appState.authFormVisibility.value){
                            Button(onClick = { appState.onStartMessagingClick() },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor, contentColor = white),
                                modifier = Modifier
                                    .size(120.dp)
                                    .align(Alignment.CenterHorizontally)) {
                                Text(text = "Start Messaging",
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontStyle = FontStyle.Italic))
                            }
                        }
                        showAuthForm(appState)
                    }

                }
            }

        // Surface(modifier = Modifier.background(Color.Cyan, Shapes.large), color = app_background) {
        //     Row(horizontalArrangement = Arrangement.Start) {
        //         CircularProgressIndicator(modifier = Modifier.padding(10.dp))
        //     }
        //     ShowCenterButton()
        // }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun showAuthForm(appState: MyAppState){
        AnimatedVisibility(
            visible = appState.authFormVisibility.value,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            Column(modifier= Modifier.padding(10.dp).shadow(10.dp).background(ComposeColor.White, AbsoluteRoundedCornerShape(10.dp)).padding(10.dp)) {
                Text(text = stringResource(R.string.found_server_on), modifier= Modifier
                    .fillMaxWidth().padding(5.dp), fontSize = 22.sp, textAlign = TextAlign.Center)
                Row(verticalAlignment = Alignment.Bottom) {
                    TextField(value = appState.username.value,
                        onValueChange = { appState.username.value = it },
                        placeholder = { stringResource(id = R.string.your_username)},
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = ComposeColor.Transparent),
                    modifier= Modifier.padding(10.dp).fillMaxWidth(0.7f))
                    Button(onClick = {
                        Toast.makeText(context, "Username: ${appState.username.value}", Toast.LENGTH_LONG).show()
                    }, modifier = Modifier.align(Alignment.Bottom),
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor, contentColor = white)) {
                        Text(text=stringResource(id = R.string.sign_in))
                    }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animationDrawable = binding.root.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()


        binding.appStartMessaging.setOnClickListener { onStartClick() }
        lifecycleScope.launch(Dispatchers.Main){
            // it is new TCP IP
            vm.liveTcpIP.collect {
                if (it.isNotEmpty()) {
                    showUIProgressIsLoading(false)
                    with(binding.mainAuthForm) {
                        alpha = 0f
                        scaleX = 0.7f
                        scaleY = 0.7f
                        visibility = View.VISIBLE
                        animate().setDuration(500).scaleX(1f).scaleY(1f).alpha(1f).start()
                    }
                    val thisUserName = vm.getSavedUsername()
                    binding.foundTcpIpText.text = getString(R.string.found_server_on, it)
                    binding.textInputUsername.setText(thisUserName)
                    binding.buttonSignIn.setOnClickListener { onLogInClick() }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main){
            // it is new TCP IP
            vm.liveSessionID.collect {
                // it is new SessionID
                if (it.isNotEmpty()) {
                    showUIProgressIsLoading(false)
                    val username = vm.getSavedUsername()
                    openChatListFragment(it, username)
                    vm.connect()
                    setUIViewsEnabled(true)
                }
            }
        }
    }

    private fun onStartClick() {
        setStartMessagingEnabled(false)
        binding.appStartMessaging.animate().scaleY(0f).scaleX(0f).setDuration(300).start()
        Toast.makeText(context, getString(R.string.toast_searching_the_server), Toast.LENGTH_LONG).show()
        showUIProgressIsLoading(true)
        vm.requestTcpIPFromUdp()
    }

    private fun openChatListFragment(sessionID: String, username: String) {
        replaceFragment(
            ChatListFragment.newInstance(sessionID, username),
            Consts.FRAGMENT_TAG_CHAT_LIST,
            false
        )
    }


    private fun setStartMessagingEnabled(enabled: Boolean) {
        val scale = if (enabled) 1f else 0f
        binding.appStartMessaging.animate().scaleY(scale).scaleX(scale).setDuration(300).start()
        binding.appStartMessaging.isEnabled = enabled
    }

    private fun setUIViewsEnabled(enabled: Boolean) {
        setStartMessagingEnabled(enabled)
        binding.buttonSignIn.isEnabled = enabled
        binding.textInputUsername.isEnabled = enabled
    }

    private fun showUIProgressIsLoading(isVisible: Boolean) {
        binding.mainProgressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun onLogInClick() {
        val inputUsernameText = binding.textInputUsername.text.toString()
        if (inputUsernameText.isBlank()) {
            Toast.makeText(context, getString(R.string.toast_create_username), Toast.LENGTH_SHORT).show()
            return
        }
        setUIViewsEnabled(false)
        showUIProgressIsLoading(true)
        vm.saveUsername(inputUsernameText)
        vm.requestSessionIDFromTCP()
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TestComposableTheme {
            InitContent()
        }
    }
}