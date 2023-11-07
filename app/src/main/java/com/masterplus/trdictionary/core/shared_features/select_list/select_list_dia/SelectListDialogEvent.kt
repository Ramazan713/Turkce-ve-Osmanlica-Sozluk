package com.masterplus.trdictionary.core.shared_features.select_list.select_list_dia

import com.masterplus.trdictionary.core.domain.model.ListView

sealed class SelectListDialogEvent{
    data class AskListDelete(val wordId: Int, val listView: ListView): SelectListDialogEvent()
}
