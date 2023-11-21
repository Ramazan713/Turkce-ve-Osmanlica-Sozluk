package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val settingsPreferences: SettingsPreferencesApp
): ViewModel(){

    private val _state = MutableStateFlow(SettingState())
    val state: StateFlow<SettingState> = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.SetDynamicTheme -> {
                viewModelScope.launch {
                    val updatedModel = _state.value.themeModel.copy(useDynamicColor = event.useDynamic)
                    themeRepo.updateThemeModel(updatedModel)
                    _state.update { it.copy(themeModel = updatedModel)}
                }
            }
            is SettingEvent.SetThemeEnum -> {
                viewModelScope.launch {
                    val updatedModel = _state.value.themeModel.copy(themeEnum = event.themeEnum)
                    themeRepo.updateThemeModel(updatedModel)
                    _state.update { it.copy(themeModel = updatedModel)}
                }
            }
            is SettingEvent.ShowDialog -> {
                _state.update { it.copy(dialogEvent = event.dialogEvent)}
            }
            is SettingEvent.ResetDefaultValues -> {
                viewModelScope.launch {
                    settingsPreferences.clear()
                    init()
                }
            }
            is SettingEvent.UseArchiveAsList -> {
                viewModelScope.launch {
                    settingsPreferences.updateData {
                        it.copy(useArchiveLikeList = event.useArchiveAsList)
                    }
                    _state.update { it.copy(useArchiveAsList = event.useArchiveAsList)}
                }
            }
            is SettingEvent.ShowSheet -> {
                _state.update { it.copy(sheetEvent = event.sheetEvent)}
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
                    _state.update { it.copy(searchResult = event.searchResult)}
                }
            }
            is SettingEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }

    private fun init(){
        viewModelScope.launch {
            val themeModel = themeRepo.getThemeModel()
            val settingsData = settingsPreferences.getData()
            _state.update { state->
                state.copy(
                    themeModel = themeModel,
                    useArchiveAsList = settingsData.useArchiveLikeList,
                    searchResult = settingsData.searchResultCount
                )
            }
        }
    }
}



















