package com.masterplus.trdictionary.features.search.domain.repo

import com.masterplus.trdictionary.features.search.domain.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepo {

    fun getFlowHistories(): Flow<List<History>>

    suspend fun insertOrUpdateHistory(query: String)

    suspend fun deleteHistories()

    suspend fun deleteHistory(history: History)

}