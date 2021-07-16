package com.edu.alterjuicechat.socket

import com.edu.alterjuicechat.socket.dto.GeneratorDto
import com.edu.alterjuicechat.socket.dto.ParserDto
import com.google.gson.Gson
import org.koin.dsl.module

object DI {
    private fun getDataStore(): DataStore{
        return DataStoreImpl()
    }
    private fun getTCPWorker(parser: ParserDto, generator: GeneratorDto, dataStore: DataStore): TCPWorker{
        return TCPWorkerImpl(parser, generator, dataStore)
    }


    val modules = module {
        single { Gson() }
        single { ParserDto(get()) }
        single { GeneratorDto(get()) }

        single { getDataStore()  }

        single { getTCPWorker(get(), get(), get()) }
        single { UDPWorker(get(), get()) }
    }
}