package com.masterplus.trdictionary.core.shared_features.theme

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.masterplus.trdictionary.core.domain.constants.KPref
import com.masterplus.trdictionary.core.domain.model.ThemeModel
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val sharedPreferences: SharedPreferences,
): ViewModel(){

    var state by mutableStateOf(ThemeModel())
        private set

    private var preferenceListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    init {
        setListener()
        init()
    }

    private fun setListener(){

        preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if(key == KPref.themeEnum.key || key == KPref.themeDynamic.key){
                state = themeRepo.getThemeModel()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    private fun init(){
        state = themeRepo.getThemeModel()
    }

    override fun onCleared() {
        super.onCleared()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
    }
}