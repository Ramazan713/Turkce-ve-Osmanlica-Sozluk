package com.masterplus.trdictionary.features.list.presentation.archive_list

import com.masterplus.trdictionary.core.domain.model.ListView

sealed class ArchiveListModalEvent{
    data class ShowSelectBottomMenu(val listView: ListView): ArchiveListModalEvent()

}
