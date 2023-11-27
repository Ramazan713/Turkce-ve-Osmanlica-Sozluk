package com.masterplus.trdictionary.core.domain.use_cases.list_words

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.data.repo.ListViewRepoFake
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import com.masterplus.trdictionary.core.domain.repo.ListWordsRepo
import com.masterplus.trdictionary.core.utils.sample_data.listView
import com.masterplus.trdictionary.core.utils.sample_data.listWords
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddFavoriteListWordsTest{

    private lateinit var listViewRepo: ListViewRepoFake
    private lateinit var listRepo: ListRepoFake
    private lateinit var listWordsRepo: ListWordsRepo

    private lateinit var addFavoriteListWords: AddFavoriteListWords

    @BeforeEach
    fun setUp(){

        listRepo = ListRepoFake()
        listViewRepo = ListViewRepoFake()
        listWordsRepo = ListWordsRepoFake()

        addFavoriteListWords = AddFavoriteListWords(listWordsRepo, listViewRepo, listRepo)
    }

    @Test
    fun ifFavoriteNotExists_addFavorite() = runTest {
        val firstItems = listRepo.items.toList()

        addFavoriteListWords(1)

        val lastItems = listRepo.items.toList()

        assertThat(firstItems).isEmpty()
        assertThat(lastItems.size).isEqualTo(1)
        assertThat(lastItems.first().isRemovable).isFalse()
        assertThat(lastItems.first().isArchive).isFalse()
    }

    @Test
    fun ifFavoriteNotExists_addWordsList() = runTest {
        val firstListWord = listWordsRepo.getListWord(1,1)

        addFavoriteListWords(1)

        val lastListWord = listWordsRepo.getListWord(1,1)

        assertThat(firstListWord).isNull()
        assertThat(lastListWord).isNotNull()
        assertThat(lastListWord!!.pos).isEqualTo(1)
    }

    @Test
    fun favoriteExists_listWordsNotExists_insertListWord() = runTest {
        val favoriteListView = listView(id = 2, isRemovable = false, contentMaxPos = 2)
        listViewRepo.listViews = listOf(favoriteListView)
        val firstListWord = listWordsRepo.getListWord(1,favoriteListView.id!!)


        addFavoriteListWords(1)

        val lastListWord = listWordsRepo.getListWord(1,favoriteListView.id!!)

        assertThat(firstListWord).isNull()
        assertThat(lastListWord).isNotNull()
        assertThat(lastListWord!!.pos).isEqualTo(favoriteListView.contentMaxPos + 1)
    }

    @Test
    fun favoriteExists_listWordsExists_deleteListWord() = runTest {
        val favoriteListView = listView(id = 2, isRemovable = false)
        listViewRepo.listViews = listOf(favoriteListView)

        val listWord = listWords(listId = favoriteListView.id!!)
        listWordsRepo.insertListWord(listWord)
        val firstListWord = listWordsRepo.getListWord(1,favoriteListView.id!!)

        addFavoriteListWords(1)

        val lastListWord = listWordsRepo.getListWord(1,favoriteListView.id!!)

        assertThat(firstListWord).isNotNull()
        assertThat(lastListWord).isNull()
    }


}