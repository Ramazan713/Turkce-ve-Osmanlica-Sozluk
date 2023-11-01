package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.settings.domain.manager.AuthManager
import com.masterplus.trdictionary.features.settings.domain.manager.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val appPreferences: AppPreferences,
    private val authManager: AuthManager,
    private val backupManager: BackupManager
): ViewModel(){

    var state by mutableStateOf(SettingState())
        private set

    private var userListenerJob: Job? = null

    init {
        init()
        listenUser()
    }

    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.SetDynamicTheme -> {
                val updatedModel = state.themeModel.copy(useDynamicColor = event.useDynamic)
                themeRepo.updateThemeModel(updatedModel)
                state = state.copy(themeModel = updatedModel)
            }
            is SettingEvent.SetThemeEnum -> {
                val updatedModel = state.themeModel.copy(themeEnum = event.themeEnum)
                themeRepo.updateThemeModel(updatedModel)
                state = state.copy(themeModel = updatedModel)
            }
            is SettingEvent.ShowDialog -> {
                state = state.copy(
                    showDialog = event.showDialog,
                    dialogEvent = event.dialogEvent
                )
            }
            is SettingEvent.ResetDefaultValues -> {
                appPreferences.clear()
                init()
                themeRepo.updateThemeModel(state.themeModel)
            }
            is SettingEvent.UseArchiveAsList -> {
                appPreferences.setItem(KPref.useArchiveLikeList,event.useArchiveAsList)
                state = state.copy(
                    useArchiveAsList = event.useArchiveAsList
                )
            }
            is SettingEvent.ShowModal -> {
                state = state.copy(
                    showModal = event.showModal,
                    modalEvent = event.modalEvent
                )
            }
            is SettingEvent.LoadData -> {
                init()
            }
            is SettingEvent.SignInLaunch -> {
                state = state.copy(settingUiEvent = SettingUiEvent.LaunchGoogleSignIn(authManager.getGoogleSignInIntent()))
            }
            is SettingEvent.SignOut -> {
                viewModelScope.launch {
                    state = state.copy(isLoading = true)
                    state = when(val result = authManager.signOut(event.backupBeforeSignOut)){
                        is Resource.Success->{
                            state.copy(message = UiText.Resource(R.string.successfully_log_out))
                        }
                        is Resource.Error->{
                            state.copy(message = result.error)
                        }
                    }
                    state = state.copy(isLoading = false)
                }
            }
            is SettingEvent.SignInWithGoogle -> {
                signIn(event)
            }
            is SettingEvent.LoadLastBackup -> {
                loadLastBackup()
            }
            is SettingEvent.NotShowBackupInitDialog -> {
                appPreferences.setItem(KPref.showBackupSectionForLogin,false)
            }
            is SettingEvent.DeleteAllUserData -> {
               viewModelScope.launch {
                   backupManager.deleteAllLocalUserData(false)
                   state = state.copy(message = UiText.Resource(R.string.successfully_deleted))
               }
            }
            is SettingEvent.SetSearchResultEnum -> {
                appPreferences.setEnumItem(KPref.searchResultCountEnum,event.searchResult)
                state = state.copy(searchResultEnum = event.searchResult)
            }
            is SettingEvent.ClearMessage -> {
                state = state.copy(message = null)
            }
            is SettingEvent.ClearUiEvent -> {
                state = state.copy(settingUiEvent = null)
            }
        }
    }

    private fun loadLastBackup(){
        val user = authManager.currentUser() ?: return
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = when(val result = backupManager.downloadLastBackup(user)){
                is Resource.Error -> {
                    state.copy(message = result.error)
                }
                is Resource.Success -> {
                    state.copy(
                        settingUiEvent = SettingUiEvent.RefreshApp,
                        message = UiText.Resource(R.string.success)
                    )
                }
            }
            state = state.copy(isLoading = false)
        }
    }


    private fun signIn(event: SettingEvent.SignInWithGoogle){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when(val result = authManager.signInWithGoogle(event.activityResult)){
                is Resource.Success->{
                    state = state.copy(isLoading = false, message = UiText.Resource(R.string.successfully_log_in))
                    val hasBackupMetas = authManager.hasBackupMetas()
                    if(hasBackupMetas && appPreferences.getItem(KPref.showBackupSectionForLogin)){
                        state = state.copy(
                            showModal = true,
                            modalEvent = SettingModalEvent.BackupSectionInit,
                        )
                    }
                }
                is Resource.Error->{
                    state = state.copy(isLoading = false, message = result.error)
                }
            }

        }
    }

    private fun init(){
        val themeModel = themeRepo.getThemeModel()
        val useArchiveAsList = appPreferences.getItem(KPref.useArchiveLikeList)
        val searchResult = appPreferences.getEnumItem(KPref.searchResultCountEnum)
        state = state.copy(
            themeModel = themeModel,
            useArchiveAsList = useArchiveAsList,
            searchResultEnum = searchResult
        )
    }

    private fun listenUser(){
        userListenerJob?.cancel()
        userListenerJob = viewModelScope.launch {
            authManager.userFlow().collectLatest { user->
                state = state.copy(
                    user = user
                )
            }
        }
    }
}



















