package com.masterplus.trdictionary.features.savepoint.presentation

import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem

data class SelectSavePointState(
    val title: String = "",
    val savePoints: List<SavePoint> = emptyList(),
    val selectedSavePoint: SavePoint? = null,
    val dropdownItems: List<SelectSavePointMenuItem> = emptyList(),
    val selectedDropdownItem: SelectSavePointMenuItem? = null,
    val showDropdownMenu: Boolean = false,
    val showDialog: Boolean = false,
    val modalDialog: SelectSavePointDialogEvent? = null,
    val uiEvent: SelectSavePointUiEvent? = null,
    val message: UiText? = null
)