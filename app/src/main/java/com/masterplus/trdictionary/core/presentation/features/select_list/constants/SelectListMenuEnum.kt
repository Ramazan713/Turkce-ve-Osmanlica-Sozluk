package com.masterplus.trdictionary.core.presentation.features.select_list.constants

import androidx.compose.ui.graphics.Color
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.IMenuItemEnum
import com.masterplus.trdictionary.core.domain.enums.IconInfo
import com.masterplus.trdictionary.core.domain.util.UiText

enum class SelectListMenuEnum: IMenuItemEnum {
    AddFavorite {
        override val title: UiText
            get() = UiText.Resource(R.string.add_favorite)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_favorite_24)
    },
    AddedFavorite {
        override val title: UiText
            get() = UiText.Resource(R.string.delete_favorite)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_favorite_24,
                tintColor = Color.Red)
    },
    AddList {
        override val title: UiText
            get() = UiText.Resource(R.string.add_to_list)

        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_library_add_24)
    },
    AddedList {
        override val title: UiText
            get() = UiText.Resource(R.string.delete_to_list)
        override val iconInfo: IconInfo
            get() = IconInfo(drawableId = R.drawable.ic_baseline_library_add_check_24)
    }
}