package com.masterplus.trdictionary.core.presentation.features.edit_savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.util.UiText

data class EditSavePointState(
    val savePoints: List<SavePoint> = emptyList(),
    val selectedSavePoint: SavePoint? = null,
    val showDialog: Boolean = false,
    val dialogEvent: EditSavePointDialogEvent? = null,
    val message: UiText? = null
)
