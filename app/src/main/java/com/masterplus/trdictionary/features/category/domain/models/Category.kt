package com.masterplus.trdictionary.features.category.domain.models

import androidx.annotation.DrawableRes
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.utils.UiText

data class Category(
    val categoryEnum: CategoryEnum,
    val title: UiText,
    @DrawableRes val resourceId: Int
)

