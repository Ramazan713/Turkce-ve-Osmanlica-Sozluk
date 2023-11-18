package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.BackupMetaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CloudSelectBackupViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val backupMetaRepo: BackupMetaRepo,
    private var authRepo: AuthRepo,
    private val appPreferences: AppPreferences
): ViewModel() {

    var state by mutableStateOf(SelectBackupState())
        private set

    private var loadDataJob: Job? = null

    init {
        loadData()
        checkRefreshButtonEnabled()
    }


    fun onEvent(event: SelectBackupEvent){
        when(event){
            is SelectBackupEvent.Refresh -> {
                refreshBackupMetas()
            }
            is SelectBackupEvent.AddTopOfBackup -> {
                downloadBackup(false, addOnLocalData = true)
            }
            is SelectBackupEvent.OverrideBackup -> {
                downloadBackup(true, addOnLocalData = false)
            }
            is SelectBackupEvent.SelectItem -> {
                state = state.copy(selectedItem = event.backupMeta)
            }
            is SelectBackupEvent.ShowDialog -> {
                state = state.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )
            }
            is SelectBackupEvent.ClearMessage -> {
               state = state.copy(message = null)
            }
            is SelectBackupEvent.ClearUiEvent -> {
                state = state.copy(uiEvent = null)
            }
        }
    }

    private fun downloadBackup(deleteAllData: Boolean, addOnLocalData: Boolean){
        viewModelScope.launch {
            val user = authRepo.currentUser() ?: return@launch
            val selectedBackup = state.selectedItem ?: return@launch
            state = state.copy(isLoading = true)
            state = when(val result = backupManager.downloadBackup(user,selectedBackup.fileName,deleteAllData,addOnLocalData)){
                is Resource.Error -> {
                    state.copy(message = result.error)
                }
                is Resource.Success -> {
                    state.copy(message = UiText.Resource(R.string.success), uiEvent = BackupSelectUiEvent.RestartApp)
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun refreshBackupMetas(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                state = state.copy(isLoading = true)
                when(val result = backupManager.refreshBackupMetas(user)){
                    is Resource.Error -> {
                        state = state.copy(message = result.error)
                    }
                    is Resource.Success -> {
                        val time = Date().time
                        appPreferences.setItem(KPref.backupMetaCounter,time)
                        state = state.copy(isRefreshEnabled = false)
                        checkRefreshButtonEnabled()
                    }
                }.let {
                    state = state.copy(isLoading = false)
                }
            }
        }
    }

    private fun checkRefreshButtonEnabled(){
        viewModelScope.launch {
            val currentTime = Date().time
            val pastTime = appPreferences.getItem(KPref.backupMetaCounter)
            val diffInMill = (pastTime + K.backupMetaRefreshMilliSeconds) - currentTime
            if(diffInMill < 0){
                state = state.copy(isRefreshEnabled = true)
                return@launch
            }
            state = state.copy(isRefreshEnabled = false)
            object : CountDownTimer(diffInMill,1000){
                override fun onTick(millisUntilFinished: Long) {
                    state = state.copy(refreshSeconds = (millisUntilFinished / 1000).toInt())
                }
                override fun onFinish() {
                    state = state.copy(isRefreshEnabled = true)
                }
            }.start()
        }
    }


    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            backupMetaRepo.getBackupMetasFlow().collectLatest { items->
                state = state.copy(items = items)
            }
        }
    }



}