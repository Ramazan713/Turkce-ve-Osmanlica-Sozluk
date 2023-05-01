package com.masterplus.trdictionary.core.domain

import kotlinx.coroutines.flow.Flow

interface ConnectivityProvider {
    fun hasConnection(): Boolean


    fun observeConnection(): Flow<Status>

    enum class Status{
        Connected, DisConnected
    }

}