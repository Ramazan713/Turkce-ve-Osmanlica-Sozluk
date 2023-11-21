package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.cloud_backup_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudBackupViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val authRepo: AuthRepo
): ViewModel(){

    private val _state = MutableStateFlow(CloudBackupState())
    val state: StateFlow<CloudBackupState> = _state.asStateFlow()


    fun makeBackup(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                _state.update { state-> state.copy(isLoading = true)}
                when(val result = backupManager.uploadBackup(user)){
                    is Resource.Error -> {
                        _state.update { it.copy(message = result.error) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(message = UiText.Resource(R.string.success)) }
                    }
                }
                _state.update { it.copy(isLoading = false)}
            }
        }
    }


    fun clearMessage(){
        _state.update { it.copy(message = null)}
    }

}