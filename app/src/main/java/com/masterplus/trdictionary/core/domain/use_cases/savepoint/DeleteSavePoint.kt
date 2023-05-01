package com.masterplus.trdictionary.core.domain.use_cases.savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import javax.inject.Inject

class DeleteSavePoint @Inject constructor(
    private val savePointRepo: SavePointRepo
) {

    suspend operator fun invoke(savePoint: SavePoint){
        savePointRepo.deleteSavePoint(savePoint)
    }
}