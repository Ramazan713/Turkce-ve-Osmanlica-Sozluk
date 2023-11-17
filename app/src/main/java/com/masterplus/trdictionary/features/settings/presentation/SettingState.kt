package com.masterplus.trdictionary.features.settings.presentation

import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User


data class SettingState(
    val themeModel: ThemeModel = ThemeModel(),
    val searchResult: Int = SettingsData().searchResultCount,
    val isLoading: Boolean = false,
    val useArchiveAsList: Boolean = false,
    val dialogEvent: SettingDialogEvent? = null,
    val sheetEvent: SettingSheetEvent? = null,
    val message: UiText? = null,
)
