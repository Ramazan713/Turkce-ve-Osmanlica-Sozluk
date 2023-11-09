package com.masterplus.trdictionary.core.di

import android.app.Application
import com.masterplus.trdictionary.core.data.local.AppDatabase
import com.masterplus.trdictionary.core.domain.use_cases.GetAppSha1UseCase
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote.TextToSpeechApiService
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote.TextToSpeechDataSource
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote.TextToSpeechDataSourceImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.TTSRepoImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordDetailRepoImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordListDetailRepoImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordListRepoImpl
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListDetailRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListRepo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordDetailDataModule {

    @Provides
    @Singleton
    fun provideWordDetailRepo(db: AppDatabase): WordDetailRepo =
        WordDetailRepoImpl(db.wordDetailDao())


    @Provides
    @Singleton
    fun provideWordListRepo(db: AppDatabase): WordListRepo =
        WordListRepoImpl(db.wordListDao())


    @Provides
    @Singleton
    fun provideWordListDetailRepo(db: AppDatabase): WordListDetailRepo =
        WordListDetailRepoImpl(db.wordListDetailDao())


    @Provides
    fun provideTTSRepo(application: Application): TTSRepo =
        TTSRepoImpl(application)



    @Singleton
    @Provides
    fun provideTTSApi(): TextToSpeechApiService =
        Retrofit.Builder()
            .baseUrl("https://texttospeech.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TextToSpeechApiService::class.java)

    @Singleton
    @Provides
    fun provideTextToSpeechDataSource(
        api: TextToSpeechApiService,
        getAppSha1UseCase: GetAppSha1UseCase,
        application: Application
    ): TextToSpeechDataSource =
        TextToSpeechDataSourceImpl(api,getAppSha1UseCase, application)

}