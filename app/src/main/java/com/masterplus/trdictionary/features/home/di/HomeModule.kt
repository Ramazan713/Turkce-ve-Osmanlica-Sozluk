package com.masterplus.trdictionary.features.home.di

import android.app.Application
import android.content.SharedPreferences
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.domain.JsonParser
import com.masterplus.trdictionary.core.domain.preferences.AppPreferences
import com.masterplus.trdictionary.features.home.data.manager.ShortInfoManagerImpl
import com.masterplus.trdictionary.features.home.data.repo.ShortInfoRepoImpl
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.features.home.domain.repo.ShortInfoRepo
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.LoadShortInfoWidget
import com.masterplus.trdictionary.features.home.domain.use_cases.widget.SendShortInfoToWidget
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
    fun provideLoadShortInfoWidget(
        shortInfoManager: ShortInfoManager,
        sendShortInfoToWidget: SendShortInfoToWidget,
    ) = LoadShortInfoWidget(
        sendShortInfoToWidget = sendShortInfoToWidget,
        shortInfoManager = shortInfoManager
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
    fun provideShortInfoManager(
        sharedPreferences: SharedPreferences,
        appPreferences: AppPreferences,
        shortInfoRepo: ShortInfoRepo
    ): ShortInfoManager =
        ShortInfoManagerImpl(
            shortInfoRepo, sharedPreferences, appPreferences
        )
}