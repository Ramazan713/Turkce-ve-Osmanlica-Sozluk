package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.enums.SearchResultEnum
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.core.domain.model.premium.PremiumProduct
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowSelectRadioItemAlertDialog
import com.masterplus.trdictionary.core.presentation.features.premium.ShowPremiumProductDialog
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.backup_select.ShowCloudSelectBackup
import com.masterplus.trdictionary.features.settings.presentation.cloud_backup_settings.ShowCloudSetting


@Composable
fun ShowSettingDialog(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
    onPremiumProductClicked: (PremiumProduct, String) -> Unit
){
    fun close(){
        onEvent(SettingEvent.ShowDialog(false))
    }

    when(val event = state.dialogEvent){
        is SettingDialogEvent.SelectThemeEnum -> {
            ShowSelectRadioItemAlertDialog(
                items = ThemeEnum.values().toList(),
                selectedItem = state.themeModel.themeEnum,
                title = stringResource(R.string.choice_theme),
                onClose = {close()},
                onApprove = {onEvent(SettingEvent.SetThemeEnum(it))}
            )
        }
        null -> {}
        is SettingDialogEvent.AskResetDefault -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_reset_default_values),
                onApproved = {onEvent(SettingEvent.ResetDefaultValues)},
                onClosed = {close()}
            )
        }
        is SettingDialogEvent.AskSignOut -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_sign_out),
                onClosed = {close()},
                onApproved = {onEvent(SettingEvent.ShowDialog(true, SettingDialogEvent.AskMakeBackupBeforeSignOut))}
            )
        }
        is SettingDialogEvent.ShowCloudBackup -> {
            ShowCloudSetting(onClosed = {close()})
        }
        is SettingDialogEvent.ShowSelectBackup -> {
            ShowCloudSelectBackup(onClosed = {close()})
        }
        is SettingDialogEvent.AskMakeBackupBeforeSignOut -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_add_backup),
                content = stringResource(R.string.unsaved_data_may_lose),
                onApproved = { onEvent(SettingEvent.SignOut(true)) },
                allowDismiss = false,
                positiveTitle = stringResource(R.string.backup_v),
                negativeTitle = stringResource(R.string.not_backup),
                onClosed = { close() },
                onDisApproved = {onEvent(SettingEvent.SignOut(false))}
            )
        }
        is SettingDialogEvent.AskDeleteAllData -> {
           ShowQuestionDialog(
               title = stringResource(R.string.are_sure_to_continue),
               content = stringResource(R.string.all_data_will_remove_not_revartable),
               onApproved = {onEvent(SettingEvent.DeleteAllUserData)},
               onClosed = { close() }
           )
        }
        SettingDialogEvent.SelectSearchResultEnum -> {
            ShowSelectRadioItemAlertDialog(
                items = SearchResultEnum.values().toList(),
                selectedItem = state.searchResultEnum,
                title = stringResource(R.string.choice_search_result_counts),
                onClose = {close()},
                onApprove = {onEvent(SettingEvent.SetSearchResultEnum(it))}
            )
        }
        is SettingDialogEvent.ShowPremiumDia -> {
            ShowPremiumProductDialog(
                premiumProduct = event.premiumProduct,
                onClosed = {close()},
                onProductClicked = onPremiumProductClicked
            )
        }
    }
}