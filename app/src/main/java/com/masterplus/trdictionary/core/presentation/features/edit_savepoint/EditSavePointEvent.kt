package com.masterplus.trdictionary.core.presentation.features.edit_savepoint

import com.masterplus.trdictionary.core.domain.model.SavePoint

sealed class EditSavePointEvent{
    data class LoadData(val saveKey: String): EditSavePointEvent()

    data class Delete(val savePoint: SavePoint): EditSavePointEvent()

    data class EditTitle(val title: String, val savePoint: SavePoint): EditSavePointEvent()

    data class Select(val savePoint: SavePoint): EditSavePointEvent()

    data class AddSavePoint(val pos: Int, val title: String, val destinationId: Int,
                            val saveKey: String): EditSavePointEvent()

    data class OverrideSavePoint(val pos: Int): EditSavePointEvent()

    data class ShowDialog(val showDialog: Boolean,
                          val dialogEvent: EditSavePointDialogEvent? = null): EditSavePointEvent()

    object ClearMessage: EditSavePointEvent()
}
