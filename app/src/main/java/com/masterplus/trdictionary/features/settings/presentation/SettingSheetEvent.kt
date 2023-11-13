package com.masterplus.trdictionary.features.settings.presentation


sealed class SettingSheetEvent {

    data class BackupSectionInit(
        val onLoadLastBackup: () -> Unit
    ): SettingSheetEvent()
}