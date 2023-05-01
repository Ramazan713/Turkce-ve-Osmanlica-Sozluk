package com.masterplus.trdictionary.features.savepoint.presentation

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem

sealed class SelectSavePointEvent{
    data class Delete(val savePoint: SavePoint): SelectSavePointEvent()

    data class EditTitle(val title: String, val savePoint: SavePoint): SelectSavePointEvent()

    data class Select(val savePoint: SavePoint?): SelectSavePointEvent()

    object LoadSavePoint: SelectSavePointEvent()

    data class ShowDialog(val showDialog: Boolean,
                          val dialogEvent: SelectSavePointDialogEvent? = null): SelectSavePointEvent()

    data class SelectDropdownMenuItem(val item: SelectSavePointMenuItem): SelectSavePointEvent()

    object ClearMessage: SelectSavePointEvent()

    object ClearUiEvent: SelectSavePointEvent()
}
