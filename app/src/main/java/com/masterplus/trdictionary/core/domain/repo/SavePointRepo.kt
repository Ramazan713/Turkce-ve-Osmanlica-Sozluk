package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.SavePoint
import kotlinx.coroutines.flow.Flow

interface SavePointRepo {

    fun getSavePointsFlow(typeIds: List<Int>): Flow<List<SavePoint>>

    fun getSavePointsFlowBySaveKey(saveKey: String):Flow<List<SavePoint>>

    suspend fun insertSavePoint(savePoint: SavePoint)

    suspend fun deleteSavePoint(savePoint: SavePoint)

    suspend fun getSavePointById(id: Int): SavePoint?

    suspend fun updateSavePoint(savePoint: SavePoint)

    suspend fun deleteSavePointsBySaveKey(saveKey: String)

}