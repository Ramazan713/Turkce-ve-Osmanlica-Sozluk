package com.masterplus.trdictionary.features.settings.presentation

import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct

sealed class SettingDialogEvent {
    data object SelectThemeEnum: SettingDialogEvent()

    data class SelectSearchResult(
        val minPos: Int,
        val maxPos: Int
    ): SettingDialogEvent()

    data object AskResetDefault: SettingDialogEvent()

    data object AskSignOut: SettingDialogEvent()

    data object ShowAuthDia: SettingDialogEvent()

    data object ShowCloudBackup: SettingDialogEvent()

    data object ShowSelectBackup: SettingDialogEvent()

    data object AskMakeBackupBeforeSignOut: SettingDialogEvent()

    data object AskDeleteAllData: SettingDialogEvent()

    data object AskDeleteAccount: SettingDialogEvent()

    data object AskReAuthenticateForDeletingAccount: SettingDialogEvent()

    data object ShowReAuthenticateForDeletingAccount: SettingDialogEvent()

    data class ShowPremiumDia(val premiumProduct: PremiumProduct?): SettingDialogEvent()
}
