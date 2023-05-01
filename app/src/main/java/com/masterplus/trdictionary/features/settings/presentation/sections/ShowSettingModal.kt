package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeFilledButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowColumnBottomContent
import com.masterplus.trdictionary.features.settings.presentation.SettingDialogEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingModalEvent
import com.masterplus.trdictionary.features.settings.presentation.SettingState


@ExperimentalMaterial3Api
@Composable
fun ShowSettingModal(
    state: SettingState,
    onEvent: (SettingEvent)->Unit,
){

    fun close(){
        onEvent(SettingEvent.ShowModal(false))
    }

    CustomModalBottomSheet(
        onDismissRequest = {close()},
        skipHalfExpanded = true
    ){
        when(state.modalEvent){
            null -> {}
            is SettingModalEvent.BackupSectionInit -> {
                ShowColumnBottomContent(
                    title = stringResource(R.string.question_download_cloud_backup)
                ){
                    PrimaryButton(
                        title = stringResource(R.string.load_last_Backup),
                        onClick = {
                            onEvent(SettingEvent.LoadLastBackup)
                            close()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        title = stringResource(R.string.show_backup_files),
                        onClick = {
                            onEvent(SettingEvent.ShowDialog(true,
                                SettingDialogEvent.ShowSelectBackup))
                            close()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    NegativeFilledButton(
                        title = stringResource(R.string.not_show_warning_again),
                        onClick = {
                            onEvent(SettingEvent.NotShowBackupInitDialog)
                            close()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    NegativeFilledButton(
                        title = stringResource(R.string.cancel),
                        onClick = { close() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}