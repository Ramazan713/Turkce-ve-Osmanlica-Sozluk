package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.util.UiText


enum class WordsPagerListBottomMenu: IMenuItemEnum {
    ShareWordsWordPager{
        override val title: UiText
            get() = UiText.Resource(R.string.share)
        override val iconInfo: IconInfo
            get() = IconInfo(R.drawable.ic_baseline_share_24)
    },
    EditSavePoint{
        override val title: UiText
            get() = UiText.Resource(R.string.add_savepoint)
        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_book_24)
    }
}
