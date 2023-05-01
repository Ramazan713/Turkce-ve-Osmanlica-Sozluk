package com.masterplus.trdictionary.features.savepoint.presentation

import com.masterplus.trdictionary.core.domain.model.SavePoint

sealed class SelectSavePointDialogEvent{

    data class AskDelete(val savePoint: SavePoint): SelectSavePointDialogEvent()
    data class EditTitle(val savePoint: SavePoint): SelectSavePointDialogEvent()
}
