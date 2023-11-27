package com.masterplus.trdictionary.core.data.local

import com.masterplus.trdictionary.core.domain.TransactionProvider
import org.junit.jupiter.api.Assertions.*

class TransactionProviderFake: TransactionProvider{
    override suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return block()
    }
}