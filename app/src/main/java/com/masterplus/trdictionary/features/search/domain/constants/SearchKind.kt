package com.masterplus.trdictionary.features.search.domain.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class SearchKind(override val title: UiText): IMenuItemEnum {
    Word(UiText.Resource(R.string.word)) {
        override val iconInfo: IconInfo?
            get() = null
    },
    Meaning(UiText.Resource(R.string.meaning)) {
        override val iconInfo: IconInfo?
            get() = null
    },
}