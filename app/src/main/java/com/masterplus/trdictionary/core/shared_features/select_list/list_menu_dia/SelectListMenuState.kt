package com.masterplus.trdictionary.core.shared_features.select_list.list_menu_dia

import com.masterplus.trdictionary.core.shared_features.select_list.constants.SelectListMenuEnum

data class SelectListMenuState(
    val listMenuItems: List<SelectListMenuEnum> = emptyList(),
    val isFavorite: Boolean = false,
    val isAddedToList: Boolean = false,
    val listIdControl: Int? = null,
    val showDialog: Boolean = false,
    val dialogEvent: SelectListMenuDialogEvent? = null
)
