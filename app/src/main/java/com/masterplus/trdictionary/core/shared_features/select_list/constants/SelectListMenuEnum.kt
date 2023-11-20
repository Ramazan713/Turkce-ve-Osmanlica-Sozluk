package com.masterplus.trdictionary.core.shared_features.select_list.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material3.MaterialTheme
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.utils.UiColor
import com.masterplus.trdictionary.core.domain.utils.UiText

enum class SelectListMenuEnum: IMenuItemEnum {
    AddFavorite {
        override val title: UiText
            get() = UiText.Resource(R.string.add_favorite)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Favorite)
    },
    AddedFavorite {
        override val title: UiText
            get() = UiText.Resource(R.string.delete_favorite)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Favorite,
                tintColor = UiColor.ComposeColor { MaterialTheme.colorScheme.error }
            )
    },
    AddList {
        override val title: UiText
            get() = UiText.Resource(R.string.add_to_list)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.LibraryAdd)
    },
    AddedList {
        override val title: UiText
            get() = UiText.Resource(R.string.delete_to_list)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.LibraryAddCheck)
    }
}