package com.masterplus.trdictionary.core.data.local

import androidx.room.withTransaction
import com.masterplus.trdictionary.core.domain.TransactionProvider

class TransactionProviderImpl(
    private val db: AppDatabase
): TransactionProvider {
    override suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}