package com.masterplus.trdictionary.features.settings.presentation.cloud_backup_settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.util.ToastHelper
import com.masterplus.trdictionary.core.presentation.components.buttons.NegativeButton
import com.masterplus.trdictionary.core.presentation.components.buttons.PrimaryButton
import com.masterplus.trdictionary.core.presentation.dialog_body.CustomDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.LoadingDialog
import com.masterplus.trdictionary.core.presentation.dialog_body.ShowQuestionDialog
import com.masterplus.trdictionary.features.settings.presentation.backup_select.ShowCloudSelectBackup
import com.masterplus.trdictionary.features.settings.presentation.components.TextIcon

@Composable
fun ShowCloudSetting(
    onClosed: ()->Unit,
    cloudViewModel: CloudBackupViewModel = hiltViewModel()
){

    val state = cloudViewModel.state
    val context = LocalContext.current

    val isVisibleSelectBackupDialog = rememberSaveable{
        mutableStateOf(false)
    }
    val isVisibleAddBackupDialog = rememberSaveable{
        mutableStateOf(false)
    }

    state.message?.let { message->
        LaunchedEffect(message){
            ToastHelper.showMessage(message,context)
            cloudViewModel.clearMessage()
        }
    }

    CustomDialog(
        onClosed = onClosed
    ){
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            item {
                TextIcon(
                    title = stringResource(R.string.cloud_backup),
                    resourceId = R.drawable.ic_baseline_cloud_24,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 3.dp)
                        .fillMaxWidth()
                )
            }
            item {
                Column {
                    PrimaryButton(
                        title = stringResource(R.string.add_backup),
                        onClick = {
                            isVisibleAddBackupDialog.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        title = stringResource(R.string.download_from_cloud),
                        onClick = {
                            isVisibleSelectBackupDialog.value = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                NegativeButton(
                    title = stringResource(R.string.cancel),
                    onClick = onClosed,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if(state.isLoading){
        LoadingDialog()
    }else if(isVisibleSelectBackupDialog.value){
        ShowCloudSelectBackup(
            onClosed = {isVisibleSelectBackupDialog.value = false}
        )
    }else if(isVisibleAddBackupDialog.value){
        ShowQuestionDialog(
            title = stringResource(R.string.are_sure_to_continue),
            content = stringResource(R.string.some_backup_files_may_change),
            onClosed = {isVisibleAddBackupDialog.value = false},
            onApproved = {cloudViewModel.makeBackup()}
        )
    }



}
