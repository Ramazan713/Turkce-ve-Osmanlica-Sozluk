package com.masterplus.trdictionary.core.data.repo

import android.os.Build
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val settingsPreferences: SettingsPreferencesApp
): ThemeRepo {
    override suspend fun getThemeModel(): ThemeModel {
        val prefData = settingsPreferences.getData()
        return ThemeModel(
            themeEnum = prefData.themeEnum,
            useDynamicColor = prefData.useThemeDynamic,
            enabledDynamicColor = hasSupportedDynamicTheme()
        )
    }

    override suspend fun updateThemeModel(themeModel: ThemeModel) {
        settingsPreferences.updateData { pref->
            pref.copy(
                themeEnum = themeModel.themeEnum,
                useThemeDynamic = themeModel.useDynamicColor
            )
        }
    }

    override fun hasSupportedDynamicTheme(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}