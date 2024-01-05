package com.masterplus.trdictionary.features.home.data.repo

import com.masterplus.trdictionary.core.domain.preferences.model.SettingsData
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceData
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.updateAndGet
import org.junit.Assert.*

class ShortInfoPreferenceFake: ShortInfoPreference {

    private val _state = MutableStateFlow(ShortInfoPreferenceData())

    override val dataFlow: Flow<ShortInfoPreferenceData>
        get() = _state


    override suspend fun updateData(transform: suspend (t: ShortInfoPreferenceData) -> ShortInfoPreferenceData): ShortInfoPreferenceData {
        return _state.getAndUpdate {
            transform(it)
        }
    }

    override suspend fun clear(): ShortInfoPreferenceData {
        return _state.updateAndGet { ShortInfoPreferenceData() }
    }

    override suspend fun getData(): ShortInfoPreferenceData {
        return _state.value
    }

}