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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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

    private val _state = MutableStateFlow(SelectBackupState())
    val state: StateFlow<SelectBackupState> = _state.asStateFlow()


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
                _state.update { state-> state.copy(selectedItem = event.backupMeta)}
            }
            is SelectBackupEvent.ShowDialog -> {
                _state.update { state->
                    state.copy(
                        showDialog = event.showDialog,
                        dialogEvent = event.dialogEvent
                    )
                }
            }
            is SelectBackupEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
            is SelectBackupEvent.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null)}
            }
        }
    }

    private fun downloadBackup(deleteAllData: Boolean, addOnLocalData: Boolean){
        viewModelScope.launch {
            val user = authRepo.currentUser() ?: return@launch
            val selectedBackup = _state.value.selectedItem ?: return@launch
            _state.update { state-> state.copy(isLoading = true)}
            when(val result = backupManager.downloadBackup(user,selectedBackup.fileName,deleteAllData,addOnLocalData)){
                is Resource.Error -> {
                    _state.update { it.copy(message = result.error)}
                }
                is Resource.Success -> {
                    _state.update { state-> state.copy(
                        message = UiText.Resource(R.string.success), uiEvent = BackupSelectUiEvent.RestartApp
                    )}
                }
            }
            _state.update { it.copy(isLoading = false)}
        }
    }

    private fun refreshBackupMetas(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                _state.update { state-> state.copy(isLoading = true)}
                when(val result = backupManager.refreshBackupMetas(user)){
                    is Resource.Error -> {
                        _state.update { it.copy(message = result.error)}
                    }
                    is Resource.Success -> {
                        val time = Date().time
                        appPreferences.setItem(KPref.backupMetaCounter,time)
                        _state.update { it.copy(isRefreshEnabled = false)}
                        checkRefreshButtonEnabled()
                    }
                }.let {
                    _state.update { it.copy(isLoading = false)}
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
                _state.update { it.copy(isRefreshEnabled = true)}
                return@launch
            }
            _state.update { state-> state.copy(isRefreshEnabled = false)}
            object : CountDownTimer(diffInMill,1000){
                override fun onTick(millisUntilFinished: Long) {
                    _state.update { it.copy(
                        refreshSeconds = (millisUntilFinished / 1000).toInt()
                    )}
                }
                override fun onFinish() {
                    _state.update { it.copy(isRefreshEnabled = true)}
                }
            }.start()
        }
    }


    private fun loadData(){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            backupMetaRepo.getBackupMetasFlow().collectLatest { items->
                _state.update { it.copy(items = items)}
            }
        }
    }



}