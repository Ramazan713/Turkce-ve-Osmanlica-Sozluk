package com.masterplus.trdictionary.core.domain

interface TransactionProvider {

    suspend fun <R> runAsTransaction(block: suspend () -> R): R

}