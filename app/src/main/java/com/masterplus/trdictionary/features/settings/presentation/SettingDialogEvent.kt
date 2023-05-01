package com.masterplus.trdictionary.features.settings.presentation

import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct

sealed class SettingDialogEvent {
    object SelectThemeEnum: SettingDialogEvent()

    object SelectSearchResultEnum: SettingDialogEvent()

    object AskResetDefault: SettingDialogEvent()

    object AskSignOut: SettingDialogEvent()

    object ShowCloudBackup: SettingDialogEvent()

    object ShowSelectBackup: SettingDialogEvent()

    object AskMakeBackupBeforeSignOut: SettingDialogEvent()

    object AskDeleteAllData: SettingDialogEvent()

    data class ShowPremiumDia(val premiumProduct: PremiumProduct?): SettingDialogEvent()
}
