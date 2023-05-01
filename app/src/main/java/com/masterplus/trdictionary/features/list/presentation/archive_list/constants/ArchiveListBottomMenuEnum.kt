package com.masterplus.trdictionary.features.list.presentation.archive_list.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.util.UiText

enum class ArchiveListBottomMenuEnum: IMenuItemEnum {

    Rename {
        override val title: UiText
            get() = UiText.Resource(R.string.rename)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_drive_file_rename_outline_24)

    },
    Delete {
        override val title: UiText
            get() = UiText.Resource(R.string.delete)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_folder_delete_24)

    },
    UnArchive{
        override val title: UiText
            get() = UiText.Resource(R.string.unarchive)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_unarchive_24)

    },
}