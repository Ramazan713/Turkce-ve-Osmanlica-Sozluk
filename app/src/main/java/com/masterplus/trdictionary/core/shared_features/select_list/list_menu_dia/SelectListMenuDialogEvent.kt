package com.masterplus.trdictionary.core.shared_features.select_list.list_menu_dia

sealed class SelectListMenuDialogEvent{
    data class AskFavoriteDelete(val wordId: Int): SelectListMenuDialogEvent()
}
