package com.edu.alterjuicechat.di

import android.content.Context.MODE_PRIVATE
import com.edu.alterjuicechat.data.repo.NAME_AUTH_REPO_DECORATOR
import com.edu.alterjuicechat.data.repo.NAME_CHAT_LIST_REPO_DECORATOR
import com.edu.alterjuicechat.data.repo.NAME_CHAT_REPO_DECORATOR
import com.edu.alterjuicechat.domain.Consts
import com.edu.alterjuicechat.viewmodels.AuthViewModel
import com.edu.alterjuicechat.viewmodels.ChatListViewModel
import com.edu.alterjuicechat.viewmodels.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.edu.alterjuicechat.data.repo.DI as repoDI
import com.edu.alterjuicechat.socket.DI as socketDI


object Modules {

    private val thingsModule = module {
        factory { androidContext().getSharedPreferences(Consts.PROFILE_PREFERENCES, MODE_PRIVATE) }
    }


    private val viewModelsModule = module {
        viewModel { ChatListViewModel(get(named(NAME_CHAT_LIST_REPO_DECORATOR)), get()) }
        viewModel { (receiverID: String) -> ChatViewModel(get(named(NAME_CHAT_REPO_DECORATOR)), receiverID) }
        viewModel { AuthViewModel(get(named(NAME_AUTH_REPO_DECORATOR)), get()) }
    }


    val allModules = listOf(
        thingsModule,
        socketDI.modules,
        repoDI.modules,
        viewModelsModule
    )
}