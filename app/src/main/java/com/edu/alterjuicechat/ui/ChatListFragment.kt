package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.edu.alterjuicechat.R
import com.edu.alterjuicechat.databinding.FragmentChatsListBinding
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.domain.model.User
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import com.edu.alterjuicechat.ui.adapters.getFormattedDate
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import com.edu.mynewcompose.ui.theme.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatListFragment : BaseFragment() {
    private lateinit var binding: FragmentChatsListBinding
    private val vm by viewModel<ChatListViewModel>(){ parametersOf(sessionID) }
    private val sessionID by lazy { arguments?.getString(Consts.FRAGMENT_PARAM_SESSION_ID)?: "" }
    private val chatsAdapter by lazy { ChatAdapter(::onChatClick) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.chatsList){
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        binding.imageUpdateChats.setOnClickListener { onUpdateChatsButtonClick() }
        lifecycleScope.launch{
            vm.users.collect {
                chatsAdapter.submitList(it + User("CustomID", "TestChatName"))
            }
        }
    }
    private fun onUpdateChatsButtonClick(){
        vm.loadUsers()
        Toast.makeText(context, R.string.toast_users_updated, Toast.LENGTH_SHORT).show()
    }
    private fun onChatClick(userInfo: User){
        replaceFragment(ChatFragment.newInstance(sessionID, userInfo.chatID, userInfo.chatName), Consts.FRAGMENT_TAG_PRIVATE_CHAT, true)
    }

    @Composable
    fun InitContent(){
        Surface(modifier = Modifier.background(primaryVariantColor)) {
            Column(modifier = Modifier.background(primaryVariantColor)){
                GetAppBarBoxWithTitle {
                    Image(painterResource(id = R.drawable.ic_round_sync_24), contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { onUpdateChatsButtonClick() }
                            .padding(6.dp)
                            .align(Alignment.CenterEnd),
                        colorFilter= ColorFilter.tint(grey))
                }
                CreateChatsList(getTestUsers())
            }
        }
    }

    fun getTestUsers(): List<User> {
        return listOf(
            User("CustomID1 CustomID1 CustomID1 CustomID1 CustomID1 CustomID1 CustomID1 CustomID1",
                "TestChatNameTestChatNameTestChatNameTestChatNameTestChatName",
                "LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE LAST MESSAGE ",
                10000),
            User("CustomID2", "TestChatName", "LAST MESSAGE", 10000),
            User("CustomID3", "TestChatName", "LAST MESSAGE", 10000),
            User("CustomID4", "TestChatName", "LAST MESSAGE", 10000),
            User("CustomID5", "TestChatName", "LAST MESSAGE", 10000),
            User("CustomID6", "TestChatName", "LAST MESSAGE", 10000)
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(sessionID: String, username: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(Consts.FRAGMENT_PARAM_SESSION_ID, sessionID)
                    putString(Consts.PROFILE_KEY_NAME, username)
                }
            }
    }


    @Composable
    fun CreateRowChatItem(userChat: User){
        LazyRow(modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .background(chat_item_background)
            .clickable { onChatClick(userChat) }
            .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly){
            item {
                Column(modifier= Modifier
                    .fillMaxHeight()
                    .fillParentMaxWidth(0.9f)){
                    Text(text = userChat.chatID, fontSize = 20.sp, fontStyle = FontStyle.Italic, modifier=Modifier.align(Alignment.Start), maxLines = 1)
                    Text(text = userChat.lastMessage, modifier=Modifier.align(Alignment.Start), maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Box(modifier = Modifier
                    .fillParentMaxHeight()
                    .fillMaxWidth()){
                    Text(userChat.unreadMessagesCount.toString(), modifier = Modifier
                        .size(30.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.Center)
                        .paddingFromBaseline(21.dp), textAlign = TextAlign.Center)
                    Text(text = getFormattedDate(userChat.lastMessageDateMilliseconds),
                        modifier=Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }

    @Composable
    fun CreateChatsList(chatList: List<User>){
        LazyColumn(modifier=Modifier.fillMaxSize()){
            items(chatList) {
                CreateRowChatItem(it)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TestComposableTheme {
            InitContent()
        }
    }
}