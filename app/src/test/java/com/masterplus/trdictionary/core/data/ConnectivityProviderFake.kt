package com.masterplus.trdictionary.core.data

import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*

class ConnectivityProviderFake: ConnectivityProvider{

    var returnedHasConnection: Boolean = true
    var returnedObserveConnection: Flow<ConnectivityProvider.Status> = flow {  }

    override fun hasConnection(): Boolean {
        return returnedHasConnection
    }

    override fun observeConnection(): Flow<ConnectivityProvider.Status> {
        return returnedObserveConnection
    }

}