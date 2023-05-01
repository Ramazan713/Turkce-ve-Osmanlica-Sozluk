package com.masterplus.trdictionary.features.home.domain.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.util.UiText

enum class HomeTopBarMenuEnum(override val title: UiText): IMenuItemEnum {

    Setting(title = UiText.Resource(R.string.settings)) {
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_settings_24)
    }

}