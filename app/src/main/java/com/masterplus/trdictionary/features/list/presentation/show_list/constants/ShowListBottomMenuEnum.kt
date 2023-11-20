package com.masterplus.trdictionary.features.list.presentation.show_list.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class ShowListBottomMenuEnum: IMenuItemEnum {
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
    Archive{
        override val title: UiText
            get() = UiText.Resource(R.string.archive_v)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Archive)

    },
    Copy{
        override val title: UiText
            get() = UiText.Resource(R.string.copy_make)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.ContentCopy)
    };

    companion object{
        fun from(isRemovable: Boolean): List<ShowListBottomMenuEnum>{
            return ShowListBottomMenuEnum.values()
                .filter {
                    if(!isRemovable){
                        return@filter listOf(Rename, Copy).contains(it)
                    }
                    true
                }
                .toList()
        }
    }

}