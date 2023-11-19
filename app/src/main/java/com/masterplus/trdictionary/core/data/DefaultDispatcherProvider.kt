package com.masterplus.trdictionary.core.data

import com.masterplus.trdictionary.core.domain.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider: DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}