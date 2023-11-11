package com.masterplus.trdictionary.features.home.domain.di

import android.app.Application
import android.content.SharedPreferences
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoRepoImpl
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import com.masterplus.trdictionary.features.home.domain.use_cases.GetShortInfo
import com.masterplus.trdictionary.features.home.domain.use_cases.GetShortInfosResult
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.LoadShortInfoWidget
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.SendShortInfoToWidget
import com.masterplus.trdictionary.features.home.domain.use_cases.ShortInfoUseCases
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.RefreshShortInfoWidget
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.ShortInfoWidgetUseCases
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
    fun provideSendShortInfoToWidget(
        application: Application,
        jsonParser: JsonParser
    ) = SendShortInfoToWidget(application, jsonParser)

    @Provides
    fun provideGetShortInfo(
        sharedPreferences: SharedPreferences,
        shortInfoRepo: ShortInfoRepo,
    ) = GetShortInfo(
        shortInfoRepo = shortInfoRepo,
        sharedPreferences = sharedPreferences,
    )

    @Provides
    fun provideLoadShortInfoWidget(
        getShortInfo: GetShortInfo,
        sendShortInfoToWidget: SendShortInfoToWidget,
    ) = LoadShortInfoWidget(
        sendShortInfoToWidget = sendShortInfoToWidget,
        getShortInfo = getShortInfo
    )



    @Provides
    @Singleton
    fun provideShortInfoWidgetUseCases(
        loadShortInfoWidget: LoadShortInfoWidget,
        sendShortInfoToWidget: SendShortInfoToWidget,
        application: Application
    ) = ShortInfoWidgetUseCases(
        loadInfoModel = loadShortInfoWidget,
        sendInfoModel = sendShortInfoToWidget,
        refreshInfoModel = RefreshShortInfoWidget(
            loadShortInfoWidget, application
        )
    )

    @Provides
    @Singleton
    fun provideShortInfoUseCases(
        getShortInfo: GetShortInfo,
        sharedPreferences: SharedPreferences,
    ) =
        ShortInfoUseCases(
            getShortInfo = getShortInfo,
            getShortInfoResult = GetShortInfosResult(
                getShortInfo = getShortInfo,
                sharedPreferences = sharedPreferences
            )
        )
}