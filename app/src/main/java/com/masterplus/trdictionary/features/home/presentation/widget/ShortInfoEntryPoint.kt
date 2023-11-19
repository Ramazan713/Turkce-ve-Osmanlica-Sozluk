package com.masterplus.trdictionary.features.home.presentation.widget

import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@EntryPoint
@InstallIn(SingletonComponent::class)
interface ShortInfoEntryPoint {

    fun getManager(): ShortInfoManager

    fun getPreferences(): ShortInfoPreference

}