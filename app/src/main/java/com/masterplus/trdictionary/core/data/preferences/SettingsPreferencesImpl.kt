package com.masterplus.trdictionary.core.data.preferences

import androidx.datastore.core.DataStore
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferences
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SettingsPreferencesImpl @Inject constructor(
    private val pref: DataStore<SettingsData>
): SettingsPreferences {

    override val settingsDataFlow: Flow<SettingsData>
        get() = pref.data


    override suspend fun updateData(transform: suspend (t: SettingsData) -> SettingsData): SettingsData{
        return pref.updateData {
            transform(it)
        }
    }

    override suspend fun clear(): SettingsData {
        return pref.updateData {
            SettingsData()
        }
    }

    override suspend fun getData(): SettingsData {
        return pref.data.first()
    }
}