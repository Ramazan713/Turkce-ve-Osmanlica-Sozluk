package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.components.SettingItem
import com.masterplus.trdictionary.features.settings.presentation.components.SettingSectionItem
import com.masterplus.trdictionary.features.settings.presentation.components.SwitchItem


@Composable
fun AdvancedSettingSection(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
    user: User?
){

    SettingSectionItem(
        title = stringResource(R.string.advanced_setting),
    ){
        SwitchItem(
            title = stringResource(R.string.use_archive_in_select),
            value = state.useArchiveAsList,
            onValueChange = {
                onEvent(SettingEvent.UseArchiveAsList(it))
            },
        )
        SettingItem(
            title = stringResource(R.string.reset_default_setting),
            resourceId = R.drawable.ic_baseline_settings_backup_restore_24,
            onClick = {
                onEvent(SettingEvent.ShowDialog(SettingDialogEvent.AskResetDefault))
            }
        )
        SettingItem(
            title = stringResource(R.string.delete_all_data),
            resourceId = R.drawable.ic_baseline_delete_forever_24,
            onClick = {
                onEvent(SettingEvent.ShowDialog(SettingDialogEvent.AskDeleteAllData))
            }
        )

        AnimatedVisibility(visible = user != null) {
            SettingItem(
                title = stringResource(R.string.delete_account),
                resourceId = R.drawable.baseline_account_circle_24,
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    onEvent(SettingEvent.ShowDialog(SettingDialogEvent.AskDeleteAccount))
                }
            )
        }

    }
}