package com.masterplus.trdictionary.core.shared_features.edit_savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint

sealed class EditSavePointDialogEvent {

    data class AskDelete(val savePoint: SavePoint): EditSavePointDialogEvent()
    data class EditTitle(val savePoint: SavePoint): EditSavePointDialogEvent()
    data class AddSavePointTitle(val title: String): EditSavePointDialogEvent()
}