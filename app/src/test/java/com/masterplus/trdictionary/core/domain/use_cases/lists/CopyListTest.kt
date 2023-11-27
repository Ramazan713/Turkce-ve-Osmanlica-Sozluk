package com.masterplus.trdictionary.core.domain.use_cases.lists

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.utils.sample_data.listModel
import com.masterplus.trdictionary.core.utils.sample_data.listView
import com.masterplus.trdictionary.core.utils.sample_data.listWords
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CopyListTest{

    private lateinit var listWordsRepo: ListWordsRepo
    private lateinit var listRepo: ListRepoFake

    private lateinit var copyList: CopyList

    @BeforeEach
    fun setUp(){
        listWordsRepo = ListWordsRepoFake()
        listRepo = ListRepoFake()

        copyList = CopyList(listRepo, listWordsRepo)
    }

    @Test
    fun copyList_insertNewList() = runTest {
        val listModel = listView()
        listRepo.insertList(listModel.toListModel())

        val firstSize = listRepo.items.size

        copyList(listModel)

        val lastSize = listRepo.items.size

        assertThat(firstSize).isEqualTo(1)
        assertThat(lastSize).isEqualTo(2)
    }

    @Test
    fun copyList_insertNewListWords() = runTest{
        var listModel = listView()
        listRepo.insertList(listModel.toListModel()).toInt().also {
            listModel = listModel.copy(id = it)
        }

        val listWords = listOf(listWords(listId = listModel.id!!), listWords(listModel.id!!, pos = 2))
        listWordsRepo.insertListWord(listWords)

        val newListId = copyList(listModel)

        val newListWords = listWordsRepo.getListWordsByListId(newListId)

        assertThat(newListId).isNotEqualTo(listModel.id)
        newListWords.forEach { listWord ->
            assertThat(listWord.listId).isEqualTo(newListId)
        }
    }

    @Test
    fun copyList_newListMustBeRemovable() = runTest {
        val listModel = listView(isRemovable = false)
        val oldListId = listRepo.insertList(listModel.toListModel()).toInt()

        val newListId = copyList(listModel)

        listRepo.getListById(oldListId)!!.let {oldList->
            assertThat(oldList.isRemovable).isFalse()
        }

        listRepo.getListById(newListId)!!.let {newList->
            assertThat(newList.isRemovable).isTrue()
        }
    }

}