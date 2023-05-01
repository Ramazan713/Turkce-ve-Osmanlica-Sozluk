package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.ThemeModel

interface ThemeRepo {
    fun getThemeModel(): ThemeModel

    fun updateThemeModel(themeModel: ThemeModel)

    fun hasSupportedDynamicTheme(): Boolean
}