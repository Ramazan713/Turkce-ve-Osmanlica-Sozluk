package com.masterplus.trdictionary.core.data.preferences

import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.updateAndGet

class SettingsPreferencesFake : SettingsPreferencesApp {

    private val _state = MutableStateFlow(SettingsData())


    override val dataFlow: Flow<SettingsData>
        get() = _state


    override suspend fun updateData(transform: suspend (t: SettingsData) -> SettingsData): SettingsData{
        return _state.getAndUpdate {
            transform(it)
        }
    }

    override suspend fun clear(): SettingsData {
        return _state.updateAndGet { SettingsData() }
    }

    override suspend fun getData(): SettingsData {
        return _state.value
    }
}