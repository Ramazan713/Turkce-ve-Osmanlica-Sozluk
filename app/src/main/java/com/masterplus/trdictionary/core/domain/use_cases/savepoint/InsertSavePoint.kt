package com.masterplus.trdictionary.core.domain.use_cases.savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.enums.AutoType
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import java.util.Calendar
import javax.inject.Inject

class InsertSavePoint @Inject constructor(
    private val savePointRepo: SavePointRepo
) {
    suspend operator fun invoke(
        itemPosIndex: Int,
        destination: SavePointDestination,
        autoType: AutoType = AutoType.Default,
        shortTitle: String? = null,
        title: String? = null,
    ){
        val date = Calendar.getInstance()

        val savePoint = SavePoint(
            title = title ?: SavePoint.getTitle(
                shortTitle?:"", autoType,date
            ),
            id = null,
            itemPosIndex = itemPosIndex,
            savePointDestination = destination,
            modifiedDate = date,
            autoType = autoType
        )

        savePointRepo.insertSavePoint(savePoint)
    }
}