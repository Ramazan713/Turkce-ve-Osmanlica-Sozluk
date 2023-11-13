package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.settings.presentation.components.SettingItem
import com.masterplus.trdictionary.features.settings.presentation.components.SettingSectionItem

@Composable
fun CloudBackupSection(
    onEvent: (SettingEvent)->Unit,
){
    SettingSectionItem(
        title = stringResource(R.string.backup_n),
    ){
        SettingItem(
            title = stringResource(R.string.cloud_backup),
            onClick = {onEvent(SettingEvent.ShowDialog(SettingDialogEvent.ShowCloudBackup))},
            resourceId = R.drawable.ic_baseline_cloud_24
        )
    }
}