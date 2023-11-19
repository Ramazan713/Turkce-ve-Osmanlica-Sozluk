package com.masterplus.trdictionary.core.di

import android.app.Application
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.data.repo.*
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferencesApp
import com.masterplus.trdictionary.core.domain.repo.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideThemeRepo(settingsPreferences: SettingsPreferencesApp): ThemeRepo =
        ThemeRepoImpl(settingsPreferences)


    @Provides
    @Singleton
    fun provideSavePointRepo(db: AppDatabase): SavePointRepo =
        SavePointRepoImpl(db.savePointDao())


    @Provides
    @Singleton
    fun provideListRepo(db: AppDatabase): ListRepo =
        ListRepoImpl(db.listDao())

    @Provides
    @Singleton
    fun provideListContentsRepo(db: AppDatabase): ListWordsRepo =
        ListWordsRepoImpl(db.listWordsDao())

    @Provides
    @Singleton
    fun provideListViewRepo(db: AppDatabase): ListViewRepo =
        ListViewRepoImpl(db.listViewDao())


    @Provides
    @Singleton
    fun providePremiumRepo(scope: CoroutineScope, application: Application) =
        PremiumRepo(application,scope)


    @Provides
    @Singleton
    fun provideAppFileRepo(application: Application,ioDispatcher: CoroutineDispatcher): AppFileRepo =
        AppFileRepoImpl(application,ioDispatcher)

}