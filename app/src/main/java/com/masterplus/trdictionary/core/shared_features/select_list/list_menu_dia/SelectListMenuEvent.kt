package com.masterplus.trdictionary.core.shared_features.select_list.list_menu_dia


sealed class SelectListMenuEvent{
    data class LoadData(val wordId: Int, val listId: Int?): SelectListMenuEvent()

    data class AddToFavorite(val wordId: Int): SelectListMenuEvent()

    data class AddOrAskFavorite(val wordId: Int): SelectListMenuEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: SelectListMenuDialogEvent? = null):
        SelectListMenuEvent()
}
