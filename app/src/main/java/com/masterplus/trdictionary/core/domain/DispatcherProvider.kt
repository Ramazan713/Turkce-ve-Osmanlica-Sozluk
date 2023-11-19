package com.masterplus.trdictionary.core.domain

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    val io: CoroutineDispatcher

    val default: CoroutineDispatcher

    val main: CoroutineDispatcher

    val unconfined: CoroutineDispatcher
}