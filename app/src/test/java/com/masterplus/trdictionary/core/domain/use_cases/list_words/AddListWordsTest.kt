package com.masterplus.trdictionary.core.domain.use_cases.list_words

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.utils.sample_data.listView
import com.masterplus.trdictionary.core.utils.sample_data.listWords
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddListWordsTest{

    private lateinit var listWordsRepo: ListWordsRepo
    private lateinit var addListWords: AddListWords

    @BeforeEach
    fun setUp(){
        listWordsRepo = ListWordsRepoFake()
        addListWords = AddListWords(listWordsRepo)
    }

    @Test
    fun addListWord_ifNotAdded() = runTest {
        val listView = listView(id = 1)
        val wordId = 2
        val oldListWord = listWordsRepo.getListWord(wordId,1)

        addListWords(listView, wordId = wordId)
        val newListWord = listWordsRepo.getListWord(wordId,1)

        assertThat(oldListWord).isNull()
        assertThat(newListWord).isNotNull()
        assertThat(newListWord?.wordId).isEqualTo(wordId)
    }

    @Test
    fun removeListWord_ifAddedBefore() = runTest {

        val listView = listView(id = 1)

        val listWord = listWords(1)
        listWordsRepo.insertListWord(listWord)
        val oldListWord = listWordsRepo.getListWord(1,1)

        addListWords(listView, wordId = 1)

        val newListWord = listWordsRepo.getListWord(1,1)

        assertThat(oldListWord).isNotNull()
        assertThat(newListWord).isNull()
    }

    @Test
    fun addListWord_increasePosByOne() = runTest {
        val pos = 4
        val listView = listView(id = 1, contentMaxPos = pos)

        addListWords(listView,1)

        val newListWord = listWordsRepo.getListWord(1,1)

        assertThat(newListWord!!.pos).isEqualTo(pos + 1)
    }

}