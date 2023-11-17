package com.masterplus.trdictionary.features.search.data

import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.core.domain.preferences.SettingsPreferences
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.search.domain.repo.HistoryRepo
import com.masterplus.trdictionary.features.search.domain.repo.SearchRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideHistoryRepo(db: AppDatabase): HistoryRepo =
        HistoryRepoImpl(db.historyDao())

    @Provides
    @Singleton
    fun provideSearchRepo(
        db: AppDatabase,
        settingsPreferences: SettingsPreferences,
        wordDetailsCompletedUseCases: WordDetailsCompletedUseCases
    ): SearchRepo =
        SearchRepoImpl(db.searchDao(),settingsPreferences,wordDetailsCompletedUseCases)
}