package com.masterplus.trdictionary.core.data.preferences

import androidx.datastore.core.DataStore
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsPreferencesImplApp @Inject constructor(
    private val pref: DataStore<SettingsData>
): SettingsPreferencesApp {

    override val dataFlow: Flow<SettingsData>
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