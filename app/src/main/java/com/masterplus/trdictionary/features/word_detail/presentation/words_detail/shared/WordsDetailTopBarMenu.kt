package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.util.UiText

enum class WordsDetailTopBarMenu(override val title: UiText): IMenuItemEnum {

    SavePoint(title = UiText.Resource(R.string.save_point)){
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_save_24)
    }
}