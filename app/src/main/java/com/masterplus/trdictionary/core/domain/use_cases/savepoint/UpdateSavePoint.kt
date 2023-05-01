package com.masterplus.trdictionary.core.domain.use_cases.savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import java.util.Calendar
import javax.inject.Inject

class UpdateSavePoint @Inject constructor(
    private val savePointRepo: SavePointRepo
) {

    suspend operator fun invoke(savePoint: SavePoint){
        val date = Calendar.getInstance()
        val updatedSavePoint = savePoint.copy(modifiedDate = date)
        savePointRepo.updateSavePoint(updatedSavePoint)
    }
}