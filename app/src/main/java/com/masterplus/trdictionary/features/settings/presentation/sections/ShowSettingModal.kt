package com.masterplus.trdictionary.features.settings.presentation.sections

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.CustomModalBottomSheet
import com.masterplus.trdictionary.core.presentation.selections.SelectMenuItemBottomContent
import com.masterplus.trdictionary.features.settings.domain.enums.BackupLoadSectionEnum
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
        onDismissRequest = { close() },
        skipHalfExpanded = true
    ){
        when(state.modalEvent){
            null -> {}
            is SettingModalEvent.BackupSectionInit -> {
                SelectMenuItemBottomContent(
                    title = stringResource(R.string.operations_download_cloud_backup),
                    items = BackupLoadSectionEnum.values().toList(),
                    onClickItem = { menuItem->
                        close()
                        when(menuItem){
                            BackupLoadSectionEnum.LoadLastBackup -> {
                                onEvent(SettingEvent.LoadLastBackup)
                            }
                            BackupLoadSectionEnum.ShowBackupFiles -> {
                                onEvent(SettingEvent.ShowDialog(true,
                                    SettingDialogEvent.ShowSelectBackup))
                            }
                            BackupLoadSectionEnum.NotShowAgain -> {
                                onEvent(SettingEvent.NotShowBackupInitDialog)
                            }
                        }
                    },
                    onClose = { close() }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowSettingModalPreview() {
    ShowSettingModal(
        state = SettingState(
            modalEvent = SettingModalEvent.BackupSectionInit,
            showModal = true
        ),
        onEvent = {}
    )
}

