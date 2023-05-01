package com.masterplus.trdictionary.core.data.local.services

import androidx.room.*
import com.masterplus.trdictionary.core.data.local.entities.SavePointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavePointDao {

    @Insert
    suspend fun insertSavePoint(savePointEntity: SavePointEntity)

    @Update
    suspend fun updateSavePoint(savePointEntity: SavePointEntity)

    @Delete
    suspend fun deleteSavePoint(savePointEntity: SavePointEntity)

    @Query("""select * from savePoints where typeId in (:typeIds) order by modifiedTimestamp desc""")
    fun getSavePointsFlow(typeIds: List<Int>):Flow<List<SavePointEntity>>

    @Query("""select * from savePoints where saveKey=:saveKey order by modifiedTimestamp desc""")
    fun getSavePointsFlowBySaveKey(saveKey: String):Flow<List<SavePointEntity>>

    @Query("""select * from savePoints where id=:id""")
    suspend fun getSavePointById(id: Int): SavePointEntity?

    @Query("""
        delete from savePoints where saveKey = :saveKey
    """)
    suspend fun deleteSavePointsBySaveKey(saveKey: String)
}