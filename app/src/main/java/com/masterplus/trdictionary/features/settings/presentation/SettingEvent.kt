package com.masterplus.trdictionary.features.settings.presentation

import androidx.activity.result.ActivityResult
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum

sealed class SettingEvent {

    object LoadData: SettingEvent()

    data class SetThemeEnum(val themeEnum: ThemeEnum): SettingEvent()

    data class SetSearchResultEnum(val searchResult: Int): SettingEvent()

    data class SetDynamicTheme(val useDynamic: Boolean): SettingEvent()

    data class ShowDialog(val showDialog: Boolean,
                          val dialogEvent: SettingDialogEvent? = null): SettingEvent()

    object ResetDefaultValues: SettingEvent()

    data class UseArchiveAsList(val useArchiveAsList: Boolean): SettingEvent()

    data class ShowModal(val showModal: Boolean,
                         val modalEvent: SettingModalEvent? = null): SettingEvent()


    object SignInLaunch: SettingEvent()

    data class SignInWithGoogle(val activityResult: ActivityResult): SettingEvent()

    data class SignOut(val backupBeforeSignOut: Boolean): SettingEvent()

    object LoadLastBackup: SettingEvent()

    object NotShowBackupInitDialog: SettingEvent()

    object DeleteAllUserData: SettingEvent()

    object ClearUiEvent: SettingEvent()

    object ClearMessage: SettingEvent()

}
