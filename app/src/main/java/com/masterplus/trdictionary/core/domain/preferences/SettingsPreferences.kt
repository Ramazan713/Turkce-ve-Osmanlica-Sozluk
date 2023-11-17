package com.masterplus.trdictionary.core.domain.preferences

import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {

    val settingsDataFlow: Flow<SettingsData>

    suspend fun updateData(transform: suspend (t: SettingsData) -> SettingsData): SettingsData

    suspend fun clear(): SettingsData

    suspend fun getData(): SettingsData
}