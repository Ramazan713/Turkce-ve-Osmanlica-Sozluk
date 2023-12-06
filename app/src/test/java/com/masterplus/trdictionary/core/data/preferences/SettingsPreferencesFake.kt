package com.masterplus.trdictionary.core.data.preferences

import android.content.Context
import androidx.datastore.core.ExperimentalMultiProcessDataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.core.domain.preferences.model.SettingsDataSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.TemporaryFolder
import java.io.File

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