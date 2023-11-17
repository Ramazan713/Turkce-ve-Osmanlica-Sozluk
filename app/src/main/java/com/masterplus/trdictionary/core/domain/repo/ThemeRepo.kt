package com.masterplus.trdictionary.core.domain.repo

import com.masterplus.trdictionary.core.domain.model.ThemeModel

interface ThemeRepo {
    suspend fun getThemeModel(): ThemeModel

    suspend fun updateThemeModel(themeModel: ThemeModel)

    fun hasSupportedDynamicTheme(): Boolean
}