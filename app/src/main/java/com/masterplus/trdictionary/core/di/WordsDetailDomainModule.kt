package com.masterplus.trdictionary.core.di

import android.app.Application
import com.masterplus.trdictionary.core.domain.ConnectivityProvider
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote.TextToSpeechDataSource
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListDetailRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordListRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.save_point_info.SavePointCategoryInfoUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.tts.TTSNetworkAudioUseCase
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.tts.TextToSpeechUseCase
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_basic.GetCategorySimpleWords
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_basic.GetListSimpleWords
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_basic.WordDetailsSimpleUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCategoryCompletedWordsPaging
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCompletedWordFlow
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetCompletedWordInfo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.GetListCompletedWordsPaging
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WordsDetailDomainModule {


    @Singleton
    @Provides
    fun provideSavepointCategoryInfo(application: Application) =
        SavePointCategoryInfoUseCases(application)

    @Provides
    fun provideTTSUseCase(dataSource: TextToSpeechDataSource, application: Application) =
        TextToSpeechUseCase(dataSource,application)

    @Provides
    fun provideTTSNetworkUseCase(ttsUseCase: TextToSpeechUseCase, connectivityProvider: ConnectivityProvider) =
        TTSNetworkAudioUseCase(ttsUseCase, connectivityProvider)



    @Singleton
    @Provides
    fun provideGetCompletedInfo(wordDetailRepo: WordDetailRepo) =
        GetCompletedWordInfo(wordDetailRepo)

    @Provides
    @Singleton
    fun provideWordDetailsSimpleUseCases(
        wordListRepo: WordListRepo,
    ) =
        WordDetailsSimpleUseCases(
            getCategorySimpleWords = GetCategorySimpleWords(
                wordListRepo = wordListRepo
            ),
            getListSimpleWords = GetListSimpleWords(
                wordListRepo = wordListRepo
            )
        )

    @Provides
    @Singleton
    fun provideWordDetailCompletedUseCases(
        wordDetailRepo: WordDetailRepo,
        getCompletedWordInfo: GetCompletedWordInfo,
        wordDetailListRepo: WordListDetailRepo
    ) =
        WordDetailsCompletedUseCases(
            getCompletedWordFlow = GetCompletedWordFlow(
                wordDetailRepo = wordDetailRepo,
                getCompletedInfo = getCompletedWordInfo
            ),
            getCategoryCompletedWordsPaging = GetCategoryCompletedWordsPaging(
                wordListDetailRepo = wordDetailListRepo,
                getCompletedInfo = getCompletedWordInfo
            ),
            getListCompletedWordsPaging = GetListCompletedWordsPaging(
                wordListDetailRepo = wordDetailListRepo,
                getCompletedInfo = getCompletedWordInfo
            ),
            completedWordInfo = getCompletedWordInfo
        )

}