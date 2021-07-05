package com.edu.alterjuicechat.di.modules

import android.content.Context
import com.edu.alterjuicechat.repo.interfaces.AuthRepo
import com.edu.alterjuicechat.repo.interfaces.ChatsRepo
import com.edu.alterjuicechat.repo.interfaces.MessagesRepo
import com.edu.alterjuicechat.repo.localImpl.AuthRepoLocal
import com.edu.alterjuicechat.repo.localImpl.ChatsRepoLocal
import com.edu.alterjuicechat.repo.localImpl.MessagesRepoLocal

object RepoModule {

    fun provideAuthRepo(): AuthRepo{
        return AuthRepoLocal()
    }
    fun provideMessagesRepo(): MessagesRepo{
        return MessagesRepoLocal()
    }

    fun provideChatsRepo(): ChatsRepo{
        return ChatsRepoLocal()
    }

}