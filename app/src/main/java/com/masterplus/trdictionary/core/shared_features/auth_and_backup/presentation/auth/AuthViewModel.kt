package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidateEmailUseCase
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidatePasswordUseCase
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferencesApp,
    private val authManager: AuthManager,
    private val backupManager: BackupManager,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
): ViewModel() {


    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()


    private var userListenerJob: Job? = null

    init {
        listenUser()
    }

    fun onEvent(event: AuthEvent){
        when(event){
            AuthEvent.DeleteAllUserData -> {
                viewModelScope.launch {
                    backupManager.deleteAllLocalUserData(false)
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_deleted)) }
                }
            }
            AuthEvent.LoadLastBackup -> {
                loadLastBackup()
            }
            is AuthEvent.ResetPassword -> {
                validateFields(email = event.email){
                    val result = authManager.resetPassword(event.email)
                    handleResourceUiText(result)
                }
            }
            is AuthEvent.SignInWithCredential -> {
                handleSignIn {
                    authManager.signInWithCredential(event.credential)
                }
            }
            is AuthEvent.SignInWithEmail -> {
                validateFields(email = event.email,password = event.password){
                    handleSignIn {
                        authManager.signInWithEmail(event.email,event.password)
                    }
                }
            }
            is AuthEvent.SignUpWithEmail -> {
                validateFields(email = event.email,password = event.password) {
                    handleSignIn {
                        authManager.signUpWithEmail(event.email,event.password)
                    }
                }
            }
            is AuthEvent.SignOut -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    when(val result = authManager.signOut(event.backupBeforeSignOut)){
                        is Resource.Success->{
                            _state.update { it.copy(message = UiText.Resource(R.string.successfully_log_out)) }
                        }
                        is Resource.Error->{
                            _state.update { it.copy(message = result.error) }
                        }
                    }
                    _state.update { state-> state.copy(isLoading = false) }
                }
            }

            AuthEvent.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }
            is AuthEvent.ShowMessage -> {
                _state.update { it.copy(message = event.message) }
            }
            is AuthEvent.ShowDialog -> {
                _state.update { it.copy(dialogEvent = event.dialogEvent) }
            }
            AuthEvent.ClearUiAction -> {
                _state.update { it.copy(uiAction = null) }
            }
        }
    }

    private fun loadLastBackup(){
        val user = authManager.currentUser() ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val result = backupManager.downloadLastBackup(user)){
                is Resource.Error -> {
                    _state.update { it.copy(message = result.error) }
                }
                is Resource.Success -> {
                    _state.update { it.copy(
                        uiAction = AuthUiAction.RefreshApp,
                        message = UiText.Resource(R.string.success)
                    ) }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun listenUser(){
        userListenerJob?.cancel()
        userListenerJob = viewModelScope.launch {
            authManager.userFlow().collectLatest { user->
                _state.update { it.copy(user = user) }
            }
        }
    }


    private fun handleResourceUiText(resource: Resource<UiText>){
        _state.update { state->
            when(resource){
                is Resource.Error -> {
                    state.copy(message = resource.data)
                }
                is Resource.Success -> {
                    state.copy(message = resource.data)
                }
            }
        }
    }

    private fun handleSignIn(call: suspend () -> Resource<UiText>){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val result = call()){
                is Resource.Success->{
                    _state.update { state->
                        state.copy(isLoading = false, message = result.data)
                    }
                    val hasBackupMetas = authManager.hasBackupMetas()
                    val showBackupSectionForLogin = settingsPreferences.getData().showBackupSectionForLogin
                    if(hasBackupMetas && showBackupSectionForLogin){
                        _state.update { state->
                            state.copy(uiAction = AuthUiAction.ShowBackupSectionForLogin)
                        }
                    }
                }
                is Resource.Error->{
                    _state.update { state->
                        state.copy(isLoading = false, message = result.error)
                    }
                }
            }
        }
    }



    private fun validateFields(
        email: String? = null,
        password: String? = null,
        call: suspend () -> Unit
    ){
        viewModelScope.launch {
            val passwordError = validatePasswordUseCase(password)
            val emailError = validateEmailUseCase(email)
            if(passwordError != null || emailError != null){
                _state.update { state->
                    state.copy(message = emailError ?: passwordError)
                }
                return@launch
            }
            call()
        }
    }
}