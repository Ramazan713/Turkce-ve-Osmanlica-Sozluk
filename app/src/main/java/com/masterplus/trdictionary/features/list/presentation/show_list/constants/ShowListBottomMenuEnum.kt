package com.masterplus.trdictionary.features.list.presentation.show_list.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class ShowListBottomMenuEnum: IMenuItemEnum {
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
    Archive{
        override val title: UiText
            get() = UiText.Resource(R.string.archive_v)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_archive_24)

    },
    Copy{
        override val title: UiText
            get() = UiText.Resource(R.string.copy_make)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_content_copy_24)
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