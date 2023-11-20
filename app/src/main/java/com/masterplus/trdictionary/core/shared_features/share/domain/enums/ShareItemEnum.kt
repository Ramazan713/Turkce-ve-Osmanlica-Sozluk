package com.masterplus.trdictionary.core.shared_features.share.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.TextFormat
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class ShareItemEnum: IMenuItemEnum {
    ShareWord {
        override val title: UiText
            get() = UiText.Resource(R.string.share_word_c)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.TextFormat)
    },
    CopyWord{
        override val title: UiText
            get() = UiText.Resource(R.string.copy_content_c)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.ContentCopy)
    },
    ShareWordMeaning {
        override val title: UiText
            get() = UiText.Resource(R.string.share_word_meaning_c)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.TextFormat)
    },

}