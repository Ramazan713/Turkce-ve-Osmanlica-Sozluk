package com.masterplus.trdictionary.core.domain.model

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum


data class ThemeModel(
    val themeEnum: ThemeEnum = ThemeEnum.defaultValue,
    val useDynamicColor: Boolean = false,
    val enabledDynamicColor: Boolean = false,
)
