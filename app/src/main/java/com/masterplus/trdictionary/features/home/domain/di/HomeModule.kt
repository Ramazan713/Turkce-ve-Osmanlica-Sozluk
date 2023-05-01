package com.masterplus.trdictionary.features.home.domain.di

import android.content.SharedPreferences
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoRepoImpl
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import com.masterplus.trdictionary.features.home.domain.use_cases.GetShortInfo
import com.masterplus.trdictionary.features.home.domain.use_cases.GetShortInfosResult
import com.masterplus.trdictionary.features.home.domain.use_cases.ShortInfoUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideShortInfoUseCases(
        sharedPreferences: SharedPreferences,
        shortInfoRepo: ShortInfoRepo
    ) =
        ShortInfoUseCases(
            getShortInfo = GetShortInfo(
                shortInfoRepo = shortInfoRepo,
                sharedPreferences = sharedPreferences,
            ),
            getShortInfoResult = GetShortInfosResult(
                getShortInfo = GetShortInfo(
                    shortInfoRepo = shortInfoRepo,
                    sharedPreferences = sharedPreferences,
                ),
                sharedPreferences = sharedPreferences
            )
        )

}