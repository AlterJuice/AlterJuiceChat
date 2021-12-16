package com.edu.alterjuicechat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
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
import com.edu.alterjuicechat.socket.UserInfo
import com.edu.alterjuicechat.ui.adapters.ChatAdapter
import com.edu.alterjuicechat.ui.adapters.getFormattedDate
import com.edu.alterjuicechat.ui.base.BaseFragment
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import com.edu.mynewcompose.ui.theme.TestComposableTheme
import com.edu.mynewcompose.ui.theme.chat_item_background
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
        binding.imageUpdateChats.setOnClickListener {
            vm.loadUsers()
            Toast.makeText(context, R.string.toast_users_updated, Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch{
            vm.users.collect {
                chatsAdapter.submitList(it + User("CustomID", "TestChatName"))
            }
        }
    }

    private fun onChatClick(userInfo: User){
        replaceFragment(ChatFragment.newInstance(sessionID, userInfo.chatID, userInfo.chatName), Consts.FRAGMENT_TAG_PRIVATE_CHAT, true)
    }

    @Composable
    fun InitContent(){
        Surface {
            Row(modifier= Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Column(modifier=Modifier.fillMaxWidth()) {
                    Text(stringResource(id = R.string.app_name),
                        modifier = Modifier.align(Alignment.Start).padding(16.dp, 0.dp))
                }
                Column {
                    Image(painterResource(id = R.drawable.ic_round_sync_24),
                        modifier = Modifier.padding(6.dp).align(Alignment.End),
                        contentDescription = "")
                }
            }
            val users = listOf(
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000),
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000),
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000),
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000),
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000),
                User("CustomID", "TestChatName", "LAST MESSAGE", 10000)
            )
            CreateChatsList(users)
        }
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
    fun CreateRowChatItem(title: String, lastMessageText: String, countUnreadMessages: Int, lastMessageDateMls: Long){
        LazyRow(modifier = Modifier
            .height(80.dp)
            .background(chat_item_background)
            .padding(10.dp, 5.dp, 10.dp, 10.dp)){
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = title, fontSize = 20.sp, fontStyle = FontStyle.Italic)
                        Text(text = lastMessageText)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = getFormattedDate(lastMessageDateMls))
                        Text(text = countUnreadMessages.toString(),
                            Modifier.size(30.dp).background(Color.White, CircleShape),
                            textAlign = TextAlign.Center)
                    }
            }
        }
    }

    @Composable
    fun CreateChatsList(chatList: List<User>){
        LazyColumn(){
            items(chatList) {
                CreateRowChatItem(it.chatID, it.lastMessage, it.unreadMessagesCount, it.lastMessageDateMilliseconds)
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