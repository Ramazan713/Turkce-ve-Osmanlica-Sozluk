package com.masterplus.trdictionary.features.word_detail.domain.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText

enum class ShareItemEnum: IMenuItemEnum {
    ShareWord {
        override val title: UiText
            get() = UiText.Resource(R.string.share_word_c)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_text_format_24)
    },
    ShareWordMeaning {
        override val title: UiText
            get() = UiText.Resource(R.string.share_word_meaning_c)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_text_format_24)
    },
    ShareLink {
        override val title: UiText
            get() = UiText.Resource(R.string.share_link_c)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_link_24)
    }
}