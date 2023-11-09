package com.masterplus.trdictionary.features.settings.presentation.cloud_backup_settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.presentation.components.DialogHeader
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.core.util.ShowLifecycleToastMessage
import com.masterplus.trdictionary.features.settings.presentation.backup_select.ShowCloudSelectBackup
import com.masterplus.trdictionary.features.settings.presentation.components.TextIcon


@Composable
fun ShowCloudSetting(
    onClosed: ()->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    cloudViewModel: CloudBackupViewModel = hiltViewModel()
){
    ShowCloudSetting(
        onClosed = onClosed,
        onMakeBackup = cloudViewModel::makeBackup,
        onClearMessage = cloudViewModel::clearMessage,
        state = cloudViewModel.state,
        windowWidthSizeClass = windowWidthSizeClass
    )
}

@Composable
fun ShowCloudSetting(
    onClosed: ()->Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    state: CloudBackupState,
    onMakeBackup: () -> Unit,
    onClearMessage: () -> Unit
){

    var isVisibleSelectBackupDialog by rememberSaveable{
        mutableStateOf(false)
    }
    var isVisibleAddBackupDialog by rememberSaveable{
        mutableStateOf(false)
    }

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = onClearMessage
    )

    CustomDialog(
        onClosed = onClosed,
        adaptiveWidthSizeClass = windowWidthSizeClass
    ){
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 2.dp)
        ) {
            item {
                DialogHeader(
                    content = {
                        TextIcon(
                            title = stringResource(R.string.cloud_backup),
                            resourceId = R.drawable.ic_baseline_cloud_24,
                        )
                    },
                    onIconClick = onClosed
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 8.dp)
                        .padding(horizontal = 8.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isVisibleAddBackupDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.add_backup))
                    }

                    Button(
                        onClick = {
                            isVisibleSelectBackupDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.download_from_cloud))
                    }
                }
            }
        }
    }

    if(state.isLoading){
        LoadingDialog()
    }else if(isVisibleSelectBackupDialog){
        ShowCloudSelectBackup(
            onClosed = { isVisibleSelectBackupDialog = false},
            windowWidthSizeClass = windowWidthSizeClass
        )
    }else if(isVisibleAddBackupDialog){
        ShowQuestionDialog(
            title = stringResource(R.string.are_sure_to_continue),
            content = stringResource(R.string.some_backup_files_may_change),
            onClosed = { isVisibleAddBackupDialog = false },
            onApproved = onMakeBackup
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCloudSettingPreview() {
    ShowCloudSetting(
        onClosed = {},
        windowWidthSizeClass = WindowWidthSizeClass.Expanded,
        state = CloudBackupState(),
        onClearMessage = {},
        onMakeBackup = {}
    )
}

