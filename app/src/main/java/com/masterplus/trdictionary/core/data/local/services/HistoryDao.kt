package com.masterplus.trdictionary.core.data.local.services

import androidx.room.*
import com.masterplus.trdictionary.core.data.local.entities.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyEntity: HistoryEntity)

    @Query("""select * from histories order by timestamp desc""")
    fun getFlowHistories(): Flow<List<HistoryEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHistory(historyEntity: HistoryEntity)

    @Query("""select * from histories where wordId = :wordId""")
    suspend fun getHistoryByWordId(wordId: Int): HistoryEntity?

    @Delete
    suspend fun deleteHistory(historyEntity: HistoryEntity)


    @Query("""delete from histories""")
    suspend fun deleteAllHistory()
}