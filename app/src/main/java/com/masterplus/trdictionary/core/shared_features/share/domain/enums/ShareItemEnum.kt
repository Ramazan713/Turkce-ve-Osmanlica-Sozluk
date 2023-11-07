package com.masterplus.trdictionary.core.shared_features.share.domain.enums

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
    CopyWord{
        override val title: UiText
            get() = UiText.Resource(R.string.copy_content_c)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.baseline_content_copy_24)
    },
    ShareWordMeaning {
        override val title: UiText
            get() = UiText.Resource(R.string.share_word_meaning_c)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_text_format_24)
    },

}