package com.masterplus.trdictionary.features.home.data.repo

import androidx.datastore.core.DataStore
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceData
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ShortInfoPreferenceImpl @Inject constructor(
    private val pref: DataStore<ShortInfoPreferenceData>
): ShortInfoPreference {

    override val dataFlow: Flow<ShortInfoPreferenceData>
        get() = pref.data

    override suspend fun updateData(transform: suspend (t: ShortInfoPreferenceData) -> ShortInfoPreferenceData): ShortInfoPreferenceData {
        return pref.updateData(transform)
    }

    override suspend fun clear(): ShortInfoPreferenceData {
        return pref.updateData {
            ShortInfoPreferenceData()
        }
    }

    override suspend fun getData(): ShortInfoPreferenceData {
        return pref.data.first()
    }
}