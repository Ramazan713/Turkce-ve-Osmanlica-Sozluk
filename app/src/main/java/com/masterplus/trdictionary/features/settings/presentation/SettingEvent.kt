package com.masterplus.trdictionary.features.settings.presentation

import com.masterplus.trdictionary.core.domain.enums.ThemeEnum

sealed interface SettingEvent {

    data object LoadData: SettingEvent

    data class SetThemeEnum(val themeEnum: ThemeEnum): SettingEvent

    data class SetSearchResultEnum(val searchResult: Int): SettingEvent

    data class SetDynamicTheme(val useDynamic: Boolean): SettingEvent

    data class ShowDialog(val dialogEvent: SettingDialogEvent? = null): SettingEvent

    data object ResetDefaultValues: SettingEvent

    data class UseArchiveAsList(val useArchiveAsList: Boolean): SettingEvent

    data class ShowSheet(val sheetEvent: SettingSheetEvent? = null): SettingEvent


    data object NotShowBackupInitDialog: SettingEvent

    data object ClearMessage: SettingEvent
}
