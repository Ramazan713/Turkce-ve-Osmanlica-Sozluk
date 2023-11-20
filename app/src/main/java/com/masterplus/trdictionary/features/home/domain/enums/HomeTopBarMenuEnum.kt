package com.masterplus.trdictionary.features.home.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class HomeTopBarMenuEnum(override val title: UiText): IMenuItemEnum {

    Setting(title = UiText.Resource(R.string.settings)) {
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Settings)
    }

}