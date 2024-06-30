package com.masterplus.trdictionary.features.home.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.features.home.data.manager.ShortInfoManagerImpl
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoPreferenceImpl
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoRepoImpl
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceData
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoPreferenceDataSerializer
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoPreference
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideShortInfoRepo(db: AppDatabase): ShortInfoRepo =
        ShortInfoRepoImpl(db.shortInfoDao())


    @Provides
    @Singleton
    fun provideShortInfoDataStore(application: Application): DataStore<ShortInfoPreferenceData> =
        MultiProcessDataStoreFactory.create(
            ShortInfoPreferenceDataSerializer(),
            produceFile = {
                File(application.cacheDir.path,"my_app/shortInfo_pb")
            }
        )

    @Provides
    @Singleton
    fun provideShortInfoPreferences(
        dataStore: DataStore<ShortInfoPreferenceData>
    ): ShortInfoPreference =
        ShortInfoPreferenceImpl(dataStore)


    @Provides
    @Singleton
    fun provideShortInfoManager(
        preferences: ShortInfoPreference,
        shortInfoRepo: ShortInfoRepo
    ): ShortInfoManager =
        ShortInfoManagerImpl(
            shortInfoRepo,
            preferences
        )
}