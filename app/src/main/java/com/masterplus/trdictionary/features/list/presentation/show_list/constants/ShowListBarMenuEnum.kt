package com.masterplus.trdictionary.features.list.presentation.show_list.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class ShowListBarMenuEnum: IMenuItemEnum {
    ShowSelectSavePoint {
        override val title: UiText
            get() = UiText.Resource(R.string.save_point)
        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_save_24)
    },
    Settings{
        override val title: UiText
            get() = UiText.Resource(R.string.settings)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_settings_24)
    }
}