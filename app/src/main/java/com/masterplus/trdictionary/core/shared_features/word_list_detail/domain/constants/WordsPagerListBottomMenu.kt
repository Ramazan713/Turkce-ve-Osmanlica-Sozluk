package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Share
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiText


enum class WordsPagerListBottomMenu: IMenuItemEnum {
    ShareWordsWordPager{
        override val title: UiText
            get() = UiText.Resource(R.string.share)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Share)
    },
    EditSavePoint{
        override val title: UiText
            get() = UiText.Resource(R.string.add_savepoint)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Book)
    }
}
