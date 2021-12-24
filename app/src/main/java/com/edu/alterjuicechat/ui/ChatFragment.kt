package com.edu.alterjuicechat.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentChatBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.model.Message
import com.edu.alterjuicechat.ui.adapters.MessagesAdapter
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import com.edu.mynewcompose.ui.theme.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatFragment : BaseFragment(){

    private lateinit var binding: FragmentChatBinding
    private val vm by viewModel<ChatViewModel>(){ parametersOf(receiverID) }


    private val sessionID by lazy {
        arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)?:""
    }
    private val receiverID by lazy{
        arguments?.getString(Consts.FRAGMENT_PARAM_USER_ID)?: ""
    }

    private val messagesAdapter by lazy{ MessagesAdapter(sessionID) }
    private val chatTitle by lazy{ arguments?.getString(Consts.FRAGMENT_PARAM_CHAT_TITLE)?: Consts.BLANK_TITLE_PLACEHOLDER }

    override fun onDestroy() {
        // Setting unread counter to 0 when exiting chat fragment
        vm.clearUnreadCounter()
        super.onDestroy()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChatTitle(chatTitle)
        binding.messagesList.apply {
            adapter = messagesAdapter
            if (layoutManager == null)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
        lifecycleScope.launch{
            vm.messages.collect {
                messagesAdapter.submitList(it)
                binding.textInputMessage.text.clear()
            }
        }
        binding.buttonSendMessage.setOnClickListener {
            binding.textInputMessage.text.toString().let {
                sendMessage(it)
                if (it.isNotBlank()) animateSendButton()
            }
        }
    }

    private fun sendMessage(msgText: String){
        if (msgText.isNotBlank()) {
            vm.sendMessage(msgText)
        }
    }
    private fun animateSendButton(){
        binding.buttonSendMessage.animate().scaleY(1.1f).scaleX(1.1f).setDuration(200).withEndAction {
            binding.buttonSendMessage.animate().scaleX(1f).scaleY(1f).start()
        }
    }


    private fun setChatTitle(title: String){
        binding.chatHeaderTitle.text = title
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TestComposableTheme {
            InitContent()
        }
    }

    class ChatMessagesState(
        val scaffoldState: ScaffoldState,
        private val resources: Resources,
    ) {
        val messages = mutableListOf<Message>()
        val inputMessage = mutableStateOf("")
        val mySenderID = "1"

    }

    @Composable
    fun rememberChatMessagesState(scaffoldState: ScaffoldState = rememberScaffoldState(), resources: Resources = LocalContext.current.resources) : ChatMessagesState{
        return remember(scaffoldState, resources, /* ... */) {
            ChatMessagesState(scaffoldState, resources, /* ... */)
        }
    }

    fun testGetMessages(): List<Message>{
        return listOf(
            Message("1", "SenderNAME", "SomeTextSomeText1_SomeText1_SomeText1_SomeText1_SomeText1_SomeText1"),
            Message("2", "SenderNAME", "SomeTextSomeText2_SomeText2_SomeText2_SomeText2_SomeText2_SomeText2"),
            Message("1", "SenderNAME", "SomeTextSomeText3_SomeText3_SomeText3_SomeText3_SomeText3_SomeText3"),
            Message("2", "SenderNAME", "SomeTextSomeText4_SomeText4_SomeText4_SomeText4_SomeText4_SomeText4"),
            Message("1", "SenderNAME", "SomeTextSomeText5_SomeText5_SomeText5_SomeText5_SomeText5_SomeText5"),
            Message("2", "SenderNAME", "SomeTextSomeText6_SomeText6_SomeText6_SomeText6_SomeText6_SomeText6"),
            Message("1", "SenderNAME", "SomeTextSomeText7_SomeText7_SomeText7_SomeText7_SomeText7_SomeText7"),
            Message("2", "SenderNAME", "SomeTextSomeText8_SomeText8_SomeText8_SomeText8_SomeText8_SomeText8"),
        )

    }

    @Composable
    fun InitContent() {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(primaryVariantColor).clipToBounds()) {
            val state = rememberChatMessagesState()
            Row(getAppBarModifier(), Arrangement.Center, Alignment.CenterVertically){
                Text("USer #1", style = AppBarTitleStyle)
            }
            Row(Modifier.fillMaxSize().weight(1f)){
                CreateListOfMessages(state, testGetMessages())
            }
            Row(Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.End)
                .background(primaryDark, Shapes.large).padding(6.dp)
            ) {
                TextField(value = state.inputMessage.value,
                    onValueChange = { state.inputMessage.value = it },
                    placeholder = { "Send message..." },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    modifier= Modifier
                        .fillMaxWidth()
                        .weight(1f)
                    )
                Image(painterResource(id = R.drawable.ic_round_send_24), contentDescription = "",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable { sendMessage(state.inputMessage.value) }
                        .align(Alignment.CenterVertically)
                        .padding(6.dp),

                    colorFilter=ColorFilter.tint(secondaryColorVariant))
            }
        }
    }


    @Composable
    fun CreateListOfMessages(state: ChatMessagesState, messages: List<Message>){
        LazyColumn(modifier=Modifier.fillMaxSize()){
            items(messages) {
                CreateMessageView(state, it)
            }
        }
    }

    @Composable
    fun LazyItemScope.CreateMessageView(state: ChatMessagesState, message: Message){
        val msgIsMine = message.senderId == state.mySenderID
        val contentArrangement: Arrangement.Horizontal = if (msgIsMine) Arrangement.Start else Arrangement.End
        val msgBackground = if (msgIsMine) message_item_mine_background_color else message_item_not_mine_background_color
        LazyRow(modifier=Modifier.fillParentMaxWidth().padding(0.dp, 5.dp), horizontalArrangement=contentArrangement){
            item {
                Text(message.text, textAlign=TextAlign.Start,
                    modifier = Modifier
                        .fillParentMaxWidth(0.6f)
                        .padding(5.dp, 0.dp)
                        .shadow(6.dp)
                        .background(msgBackground, Shapes.medium)
                        .padding(5.dp, 0.dp)
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sessionID: String, userId: String, userName: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(Consts.FRAGMENT_PARAM_CHAT_TITLE, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_NAME, userName)
                    putString(Consts.FRAGMENT_PARAM_USER_ID, userId)
                    putString(Consts.FRAGMENT_PARAM_SESSION_ID, sessionID)
                }
            }
    }
}