package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.cloud_backup_settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloudBackupViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val authRepo: AuthRepo
): ViewModel(){

    var state by mutableStateOf(CloudBackupState())
        private set

    fun makeBackup(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                state = state.copy(isLoading = true)
                state = when(val result = backupManager.uploadBackup(user)){
                    is Resource.Error -> {
                        state.copy(message = result.error)
                    }
                    is Resource.Success -> {
                        state.copy(message = UiText.Resource(R.string.success))
                    }
                }
                state = state.copy(isLoading = false)
            }
        }
    }


    fun clearMessage(){
        state = state.copy(message = null)
    }

}