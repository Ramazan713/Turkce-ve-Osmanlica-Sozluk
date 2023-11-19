package com.masterplus.trdictionary.core.shared_features.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferencesApp,
    private val themeRepo: ThemeRepo
): ViewModel(){

    private val _state = MutableStateFlow(ThemeModel())
    val state: StateFlow<ThemeModel> = _state.asStateFlow()

    init {
        setListener()
    }

    private fun setListener(){
        viewModelScope.launch {
            settingsPreferences.dataFlow
                .map {
                    ThemeModel(it.themeEnum,it.useThemeDynamic,themeRepo.hasSupportedDynamicTheme())
                }
                .distinctUntilChanged()
                .collectLatest {themeModel->
                    _state.update { themeModel}
                }
        }
    }
}