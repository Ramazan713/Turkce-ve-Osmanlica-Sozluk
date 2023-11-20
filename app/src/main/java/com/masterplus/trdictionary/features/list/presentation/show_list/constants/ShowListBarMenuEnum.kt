package com.masterplus.trdictionary.features.list.presentation.show_list.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class ShowListBarMenuEnum: IMenuItemEnum {
    ShowSelectSavePoint {
        override val title: UiText
            get() = UiText.Resource(R.string.save_point)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Save)
    },
    Settings{
        override val title: UiText
            get() = UiText.Resource(R.string.settings)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Settings)
    }
}