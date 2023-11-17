package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferences
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val settingsPreferences: SettingsPreferences
): ViewModel(){

    var state by mutableStateOf(SettingState())
        private set

    init {
        init()
    }

    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.SetDynamicTheme -> {
                viewModelScope.launch {
                    val updatedModel = state.themeModel.copy(useDynamicColor = event.useDynamic)
                    themeRepo.updateThemeModel(updatedModel)
                    state = state.copy(themeModel = updatedModel)
                }
            }
            is SettingEvent.SetThemeEnum -> {
                viewModelScope.launch {
                    val updatedModel = state.themeModel.copy(themeEnum = event.themeEnum)
                    themeRepo.updateThemeModel(updatedModel)
                    state = state.copy(themeModel = updatedModel)
                }
            }
            is SettingEvent.ShowDialog -> {
                state = state.copy(
                    dialogEvent = event.dialogEvent
                )
            }
            is SettingEvent.ResetDefaultValues -> {
                viewModelScope.launch {
                    settingsPreferences.clear()
                    init()
                    themeRepo.updateThemeModel(state.themeModel)
                }
            }
            is SettingEvent.UseArchiveAsList -> {
                viewModelScope.launch {
                    settingsPreferences.updateData {
                        it.copy(useArchiveLikeList = event.useArchiveAsList)
                    }
                    state = state.copy(useArchiveAsList = event.useArchiveAsList)
                }
            }
            is SettingEvent.ShowSheet -> {
                state = state.copy(sheetEvent = event.sheetEvent)
            }
            is SettingEvent.LoadData -> {
                init()
            }
            is SettingEvent.NotShowBackupInitDialog -> {
                viewModelScope.launch {
                    settingsPreferences.updateData {
                        it.copy(showBackupSectionForLogin = false)
                    }
                }
            }
            is SettingEvent.SetSearchResultEnum -> {
                viewModelScope.launch {
                    settingsPreferences.updateData {
                        it.copy(searchResultCount = event.searchResult)
                    }
                    state = state.copy(searchResult = event.searchResult)
                }
            }
            is SettingEvent.ClearMessage -> {
                state = state.copy(message = null)
            }
        }
    }

    private fun init(){
        viewModelScope.launch {
            val themeModel = themeRepo.getThemeModel()
            val settingsData = settingsPreferences.getData()
            state = state.copy(
                themeModel = themeModel,
                useArchiveAsList = settingsData.useArchiveLikeList,
                searchResult = settingsData.searchResultCount
            )
        }
    }
}



















