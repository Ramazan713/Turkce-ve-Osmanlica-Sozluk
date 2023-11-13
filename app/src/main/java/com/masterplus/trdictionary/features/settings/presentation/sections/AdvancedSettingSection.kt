package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent

import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState
import com.masterplus.trdictionary.features.settings.presentation.components.SettingItem
import com.masterplus.trdictionary.features.settings.presentation.components.SettingSectionItem
import com.masterplus.trdictionary.features.settings.presentation.components.SwitchItem


@Composable
fun AdvancedSettingSection(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
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
    }
}