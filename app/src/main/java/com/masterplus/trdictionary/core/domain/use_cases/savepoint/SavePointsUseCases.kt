package com.masterplus.trdictionary.core.domain.use_cases.savepoint

data class SavePointsUseCases(
    val deleteSavePoint: DeleteSavePoint,
    val insertSavePoint: InsertSavePoint,
    val updateSavePoint: UpdateSavePoint,
    val getSavePoints: GetSavePointsByType
)
