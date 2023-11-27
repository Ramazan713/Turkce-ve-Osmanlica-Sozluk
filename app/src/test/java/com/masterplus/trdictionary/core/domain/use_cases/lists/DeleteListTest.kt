package com.masterplus.trdictionary.core.domain.use_cases.lists

import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.masterplus.trdictionary.core.data.local.TransactionProviderFake
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.data.repo.SavePointRepoFake
import com.masterplus.trdictionary.core.domain.TransactionProvider
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import com.masterplus.trdictionary.core.utils.sample_data.listView
import com.masterplus.trdictionary.core.utils.sample_data.listWords
import com.masterplus.trdictionary.core.utils.sample_data.savePoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteListTest{

    private lateinit var savePointRepo: SavePointRepo
    private lateinit var listWordsRepo: ListWordsRepo
    private lateinit var listRepo: ListRepo
    private lateinit var transactionProvider: TransactionProvider
    private lateinit var deleteList: DeleteList


    @BeforeEach
    fun setUp(){

        savePointRepo = SavePointRepoFake()
        listWordsRepo = ListWordsRepoFake()
        listRepo = ListRepoFake()
        transactionProvider = TransactionProviderFake()

        deleteList = DeleteList(
            listRepo, listWordsRepo, savePointRepo, transactionProvider
        )
    }


    @Test
    fun deleteList_success() = runTest {
        val listView = listView()

        val listId = listRepo.insertList(listView.toListModel()).toInt()
        val firstListModel = listRepo.getListById(listId)

        deleteList.invoke(listView)

        val lastListModel = listRepo.getListById(listId)

        assertThat(firstListModel).isNotNull()
        assertThat(lastListModel).isNull()
    }

    @Test
    fun delete_listWordsAndSavePoint_success() = runTest {
        var listView = listView()
        val listId = listRepo.insertList(listView.toListModel()).toInt().also {
            listView = listView.copy(id = it)
        }

        val listWord = listWords(listId = listId, wordId = 1)
        val savePoint = savePoint(savePointDestination = SavePointDestination.List(listId))

        savePointRepo.insertSavePoint(savePoint)
        listWordsRepo.insertListWord(listWord)

        val firstSavePoint = savePointRepo.getSavePointById(savePoint.id!!)
        val firstListWords = listWordsRepo.getListWord(1,listId)

        deleteList.invoke(listView)

        val lastSavePoint = savePointRepo.getSavePointById(savePoint.id!!)
        val lastListWords = listWordsRepo.getListWord(1,listId)


        assertThat(firstSavePoint).isNotNull()
        assertThat(firstListWords).isNotNull()

        assertThat(lastSavePoint).isNull()
        assertThat(lastListWords).isNull()
    }

}