package com.masterplus.trdictionary.features.list.presentation.archive_list.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class ArchiveListBottomMenuEnum: IMenuItemEnum {

    Rename {
        override val title: UiText
            get() = UiText.Resource(R.string.rename)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Outlined.DriveFileRenameOutline)

    },
    Delete {
        override val title: UiText
            get() = UiText.Resource(R.string.delete)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.FolderDelete)

    },
    UnArchive{
        override val title: UiText
            get() = UiText.Resource(R.string.unarchive)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Unarchive)

    },
}