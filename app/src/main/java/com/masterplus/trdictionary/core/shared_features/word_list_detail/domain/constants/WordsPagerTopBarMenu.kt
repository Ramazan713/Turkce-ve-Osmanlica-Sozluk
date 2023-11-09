package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class WordsPagerTopBarMenu(override val title: UiText): IMenuItemEnum {

    SavePoint(title = UiText.Resource(R.string.save_point)){
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Save)
    }
}