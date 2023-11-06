package com.masterplus.trdictionary.core.domain.di

import com.masterplus.trdictionary.core.data.local.TransactionProvider
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.use_cases.list_words.AddFavoriteListWords
import com.masterplus.trdictionary.core.domain.use_cases.list_words.AddListWords
import com.masterplus.trdictionary.core.domain.use_cases.list_words.GetSelectableLists
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import com.masterplus.trdictionary.core.domain.use_cases.lists.*
import com.masterplus.trdictionary.core.domain.use_cases.savepoint.*
import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideListUseCases(listRepo: ListRepo,
                            listViewRepo: ListViewRepo,
                            listWordsRepo: ListWordsRepo,
                            savePointRepo: SavePointRepo,
                            transactionProvider: TransactionProvider
    ) =
        ListUseCases(
            insertList = InsertList(listRepo),
            updateList = UpdateList(listRepo),
            deleteList = DeleteList(listRepo,listWordsRepo,savePointRepo,transactionProvider),
            getLists = GetLists(listViewRepo),
            copyList = CopyList(listRepo,listWordsRepo),
        )


    @Provides
    @Singleton
    fun provideContentListsUseCases(
        listRepo: ListRepo,
        listViewRepo: ListViewRepo,
        listWordsRepo: ListWordsRepo
    ) =
        ListWordsUseCases(
            addFavoriteListWords = AddFavoriteListWords(listWordsRepo, listViewRepo, listRepo),
            addListWords = AddListWords(listWordsRepo),
            getSelectableLists = GetSelectableLists(listViewRepo)
        )


    @Provides
    @Singleton
    fun provideSavePointUseCases(savePointRepo: SavePointRepo) =
        SavePointsUseCases(
            insertSavePoint = InsertSavePoint(savePointRepo),
            updateSavePoint = UpdateSavePoint(savePointRepo),
            deleteSavePoint = DeleteSavePoint(savePointRepo),
            getSavePoints = GetSavePointsByType(savePointRepo)
        )

    @Provides
    @Singleton
    fun provideShareWordUseCases(
        wordDetailRepo: WordDetailRepo,
    ) = ShareWordUseCases(wordDetailRepo)
}