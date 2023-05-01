package com.masterplus.trdictionary.core.domain.use_cases.savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavePointsByType @Inject constructor(
    private val savePointRepo: SavePointRepo
){

    operator fun invoke(typeIds: List<Int>): Flow<List<SavePoint>>{
        return savePointRepo.getSavePointsFlow(typeIds)
    }

    operator fun invoke(saveKey: String): Flow<List<SavePoint>>{
        return savePointRepo.getSavePointsFlowBySaveKey(saveKey)
    }
}