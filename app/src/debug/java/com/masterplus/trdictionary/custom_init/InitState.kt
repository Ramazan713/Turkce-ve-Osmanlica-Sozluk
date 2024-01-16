package com.masterplus.trdictionary.custom_init

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum

data class InitState(
    val widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    val displayFeatures: List<DisplayFeature> = emptyList(),
    val destinationRoute: String? = null
)
