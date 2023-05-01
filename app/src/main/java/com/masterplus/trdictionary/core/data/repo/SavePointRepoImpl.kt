package com.masterplus.trdictionary.core.data.repo

import com.masterplus.trdictionary.core.data.local.services.SavePointDao
import com.masterplus.trdictionary.core.data.local.mapper.toSavePoint
import com.masterplus.trdictionary.core.data.local.mapper.toSavePointEntity
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavePointRepoImpl @Inject constructor(
    private val savePointDao: SavePointDao
): SavePointRepo {
    override fun getSavePointsFlow(typeIds: List<Int>): Flow<List<SavePoint>> {
        return savePointDao.getSavePointsFlow(typeIds).map { items->
            items.map { it.toSavePoint() }
        }
    }

    override fun getSavePointsFlowBySaveKey(saveKey: String): Flow<List<SavePoint>> {
        return savePointDao.getSavePointsFlowBySaveKey(saveKey).map { items->
            items.map { it.toSavePoint() }
        }
    }

    override suspend fun insertSavePoint(savePoint: SavePoint) {
        savePointDao.insertSavePoint(savePoint.toSavePointEntity())
    }

    override suspend fun deleteSavePoint(savePoint: SavePoint) {
        savePointDao.deleteSavePoint(savePoint.toSavePointEntity())
    }

    override suspend fun getSavePointById(id: Int): SavePoint? {
        return savePointDao.getSavePointById(id)?.toSavePoint()
    }

    override suspend fun updateSavePoint(savePoint: SavePoint) {
        savePointDao.updateSavePoint(savePoint.toSavePointEntity())
    }

    override suspend fun deleteSavePointsBySaveKey(saveKey: String) {
        savePointDao.deleteSavePointsBySaveKey(saveKey)
    }
}