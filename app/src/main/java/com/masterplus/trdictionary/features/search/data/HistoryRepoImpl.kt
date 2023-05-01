package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import com.masterplus.trdictionary.features.search.data.mapper.toHistory
import com.masterplus.trdictionary.core.data.local.services.HistoryDao
import com.masterplus.trdictionary.features.search.data.mapper.toHistoryEntity
import com.masterplus.trdictionary.features.search.domain.model.History
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class HistoryRepoImpl @Inject constructor(
    private val historyDao: HistoryDao
): HistoryRepo {

    override fun getFlowHistories(): Flow<List<History>> {
        return historyDao.getFlowHistories().map { items->items.map { it.toHistory() } }
    }

    override suspend fun insertOrUpdateHistory(query: String,wordId: Int) {
        val history = historyDao.getHistoryByWordId(wordId)
        val timeStamp = Date().time
        val updatedHistory: HistoryEntity = if(history!=null){
            HistoryEntity(id = history.id, content = query, timeStamp = timeStamp, wordId = wordId)
        }else{
            HistoryEntity(content = query, timeStamp = timeStamp, id = null, wordId = wordId)
        }
        historyDao.insertHistory(updatedHistory)
    }

    override suspend fun deleteHistories() {
        historyDao.deleteAllHistory()
    }

    override suspend fun deleteHistory(history: History) {
        historyDao.deleteHistory(history.toHistoryEntity())
    }
}