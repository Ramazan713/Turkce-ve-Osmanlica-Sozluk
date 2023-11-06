package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText


enum class WordListBottomMenu: IMenuItemEnum {
    ShareText{
        override val title: UiText
            get() = UiText.Resource(R.string.share)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_share_24)
    },
    CopyText{
        override val title: UiText
            get() = UiText.Resource(R.string.copy_content)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_content_copy_24)
    },
    EditSavePoint{
        override val title: UiText
            get() = UiText.Resource(R.string.add_savepoint)
        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_book_24)
    }
}
