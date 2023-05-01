package com.masterplus.trdictionary.core.data.repo

import android.os.Build
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val appPreferences: AppPreferences
): ThemeRepo {
    override fun getThemeModel(): ThemeModel {
        return ThemeModel(
            themeEnum = appPreferences.getEnumItem(KPref.themeEnum),
            useDynamicColor = appPreferences.getItem(KPref.themeDynamic),
            enabledDynamicColor = hasSupportedDynamicTheme()
        )
    }

    override fun updateThemeModel(themeModel: ThemeModel) {
        appPreferences.setEnumItem(KPref.themeEnum,themeModel.themeEnum)
        appPreferences.setItem(KPref.themeDynamic,themeModel.useDynamicColor)
    }

    override fun hasSupportedDynamicTheme(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}