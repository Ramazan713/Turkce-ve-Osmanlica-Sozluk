package com.masterplus.trdictionary.features.settings.presentation.backup_select

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.extensions.refreshApp
import com.masterplus.trdictionary.features.settings.presentation.components.SelectableText
import com.masterplus.trdictionary.features.settings.presentation.components.TextIcon
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShowCloudSelectBackup(
    onClosed: ()->Unit,
    selectBackupViewModel: CloudSelectBackupViewModel = hiltViewModel()
){

    val state = selectBackupViewModel.state
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    state.message?.let { message->
        LaunchedEffect(message){
            ToastHelper.showMessage(message,context)
            selectBackupViewModel.onEvent(SelectBackupEvent.ClearMessage)
        }
    }

    state.uiEvent?.let { uiEvent->
        LaunchedEffect(uiEvent,lifecycle){
            snapshotFlow { uiEvent }
                .flowWithLifecycle(lifecycle)
                .collectLatest { event->
                    when(event){
                        BackupSelectUiEvent.RestartApp -> {
                            context.refreshApp()
                        }
                    }
                }
        }
    }

    CustomDialog(
        onClosed = onClosed
    ){
        LazyColumn(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 13.dp)
        ) {
            item {
                TextIcon(
                    title = stringResource(R.string.download_from_cloud),
                    resourceId = R.drawable.ic_baseline_cloud_download_24,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 30.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if(!state.isRefreshEnabled){
                        Text(state.refreshSeconds.toString(), style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.error
                        ))
                    }
                    IconButton(
                        onClick = {
                            selectBackupViewModel.onEvent(SelectBackupEvent.Refresh)
                        },
                        enabled = state.isRefreshEnabled,
                    ){
                        Icon(painter = painterResource(R.drawable.ic_baseline_refresh_24),contentDescription = null)
                    }
                }
            }

            if(state.items.isEmpty()){
                item {
                    Text(
                        stringResource(R.string.empty_backup_select),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 13.dp)
                    )
                }
            }

            items(
                state.items,
                key = {item->item.id?:0}
            ){item->
                SelectableText(
                    title = item.title,
                    isSelected = item == state.selectedItem,
                    onClick = {
                        selectBackupViewModel.onEvent(SelectBackupEvent.SelectItem(item))
                    },
                    modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
                        .fillMaxWidth()
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    PrimaryButton(
                        title = stringResource(R.string.override),
                        onClick = {
                            selectBackupViewModel.onEvent(SelectBackupEvent.ShowDialog(true,
                                BackupSelectDialogEvent.AskOverrideBackup))},
                        modifier = Modifier.fillMaxWidth()
                    )
                    PrimaryButton(
                        title = stringResource(R.string.add_on),
                        onClick = {
                            selectBackupViewModel.onEvent(SelectBackupEvent.ShowDialog(true,
                                BackupSelectDialogEvent.AskAddOnBackup)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    NegativeButton(
                        title = stringResource(R.string.cancel),
                        onClick = onClosed,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }

    if(state.showDialog){
        ShowDialog(
            event = state.dialogEvent,
            onEvent = {selectBackupViewModel.onEvent(it)}
        )
    }

    if(state.isLoading){
        LoadingDialog()
    }


}

@Composable
fun ShowDialog(
    event: BackupSelectDialogEvent?,
    onEvent: (SelectBackupEvent)->Unit
){
    fun close(){
        onEvent(SelectBackupEvent.ShowDialog(false))
    }

    when(event){
        BackupSelectDialogEvent.AskAddOnBackup -> {
            ShowQuestionDialog(
                title = stringResource(R.string.are_sure_to_continue),
                content = stringResource(R.string.add_on_backup_warning),
                onClosed = { close() },
                onApproved = { onEvent(SelectBackupEvent.AddTopOfBackup) }
            )
        }
        BackupSelectDialogEvent.AskOverrideBackup -> {
            ShowQuestionDialog(
                title = stringResource(R.string.are_sure_to_continue),
                content = stringResource(R.string.override_backup_warning),
                onClosed = { close() },
                onApproved = { onEvent(SelectBackupEvent.OverrideBackup) }
            )
        }
        null -> {}
    }

}


