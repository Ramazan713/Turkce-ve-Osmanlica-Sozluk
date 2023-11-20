package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.extensions.refreshApp
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import com.masterplus.trdictionary.features.settings.presentation.components.SelectableText
import com.masterplus.trdictionary.features.settings.presentation.components.TextIcon
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShowCloudSelectBackup(
    onClosed: ()->Unit,
    selectBackupViewModel: CloudSelectBackupViewModel = hiltViewModel(),
    windowWidthSizeClass: WindowWidthSizeClass
){
    val state by selectBackupViewModel.state.collectAsStateWithLifecycle()
    ShowCloudSelectBackup(
        onClosed = onClosed,
        state = state,
        onEvent = selectBackupViewModel::onEvent,
        windowWidthSizeClass = windowWidthSizeClass
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShowCloudSelectBackup(
    onClosed: () -> Unit,
    state: SelectBackupState,
    onEvent: (SelectBackupEvent) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass
){

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(SelectBackupEvent.ClearMessage) }
    )

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
        onClosed = onClosed,
        adaptiveWidthSizeClass = windowWidthSizeClass
    ){
        Column(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp)
        ) {
            DialogHeader(
                content = {
                    TextIcon(
                        title = stringResource(R.string.download_from_cloud),
                        resourceId = R.drawable.ic_baseline_cloud_download_24,
                    )
                },
                onIconClick = onClosed
            )
            LazyColumn(
                modifier = Modifier
            ) {

                item {
                    GetRefreshInfo(state = state, onEvent = onEvent)
                }

                if(state.items.isEmpty()){
                    item {
                        Text(
                            stringResource(R.string.empty_backup_select),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }
                }

                items(
                    state.items,
                    key = {item -> item.id ?: 0}
                ){item->
                    SelectableText(
                        title = item.title,
                        isSelected = item == state.selectedItem,
                        onClick = {
                            onEvent(SelectBackupEvent.SelectItem(item))
                        },
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                            .fillMaxWidth()
                    )
                }
            }

            GetButtons(onEvent = onEvent)
        }
    }

    if(state.showDialog){
        ShowDialog(
            event = state.dialogEvent,
            onEvent = {onEvent(it)}
        )
    }

    if(state.isLoading){
        LoadingDialog()
    }
}


@Composable
private fun GetRefreshInfo(
    state: SelectBackupState,
    onEvent: (SelectBackupEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = { onEvent(SelectBackupEvent.Refresh) },
            enabled = state.isRefreshEnabled,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_refresh_24),
                contentDescription = stringResource(id = R.string.refresh)
            )
            Text(
                text = stringResource(id = R.string.refresh),
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        if(!state.isRefreshEnabled){
            Text(state.refreshSeconds.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
private fun GetButtons(
    onEvent: (SelectBackupEvent) -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                onEvent(
                    SelectBackupEvent.ShowDialog(
                        true,
                        BackupSelectDialogEvent.AskAddOnBackup
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.add_on))
        }

        Button(
            onClick = {
                onEvent(
                    SelectBackupEvent.ShowDialog(
                        true,
                        BackupSelectDialogEvent.AskOverrideBackup
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.override))
        }
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


@Preview(showBackground = true)
@Composable
fun ShowCloudSelectBackupPreview() {
    ShowCloudSelectBackup(
        onClosed = {},
        onEvent = {},
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        state = SelectBackupState(
            items = listOf(
                BackupMeta(id = 1, fileName = "file", updatedDate = 1000L, title = "Title 1"),
                BackupMeta(id = 2, fileName = "file", updatedDate = 1000L, title = "Title 1")
            ),
            isRefreshEnabled = false,
            refreshSeconds = 10
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ShowCloudSelectBackupPreview2() {
    ShowCloudSelectBackup(
        onClosed = {},
        onEvent = {},
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        state = SelectBackupState()
    )
}

