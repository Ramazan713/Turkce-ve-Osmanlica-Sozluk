package com.masterplus.trdictionary.features.settings.data.di

import com.masterplus.trdictionary.features.settings.data.GsonParser
import com.masterplus.trdictionary.features.settings.domain.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataModule {

    @Provides
    @Singleton
    fun provideJsonParser(): JsonParser = GsonParser()

}