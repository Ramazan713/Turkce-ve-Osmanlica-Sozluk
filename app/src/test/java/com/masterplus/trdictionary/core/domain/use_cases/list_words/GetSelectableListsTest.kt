package com.masterplus.trdictionary.core.domain.use_cases.list_words

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.repo.ListViewRepoFake
import com.masterplus.trdictionary.core.utils.sample_data.listView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSelectableListsTest{

    private lateinit var listViewRepo: ListViewRepoFake
    private lateinit var getSelectableLists: GetSelectableLists

    @BeforeEach
    fun setUp(){
        listViewRepo = ListViewRepoFake()
        getSelectableLists = GetSelectableLists(listViewRepo)
    }

    @Test
    fun ifUseArchiveAsList_isNull_returnsNonArchiveRemovableLists() = runTest {

        val archiveList = listView(isArchive = true, isRemovable = true)
        val archiveNonRemovableList = listView(isArchive = true, isRemovable = false)
        val nonArchiveRemovableList = listView(isRemovable = true, isArchive = false)
        listViewRepo.listViews = listOf(archiveList,archiveNonRemovableList,nonArchiveRemovableList)

        val result = getSelectableLists.invoke(null,1).first().map { it.listView }

        assertThat(result).doesNotContain(archiveList)
        assertThat(result).doesNotContain(archiveNonRemovableList)
        assertThat(result).contains(nonArchiveRemovableList)
    }

    @Test
    fun ifUseArchiveAsList_isTrue_returnsAllRemovableLists() = runTest {

        val archiveList = listView(isArchive = true, isRemovable = true)
        val archiveNonRemovableList = listView(isArchive = true, isRemovable = false)
        val nonArchiveRemovableList = listView(isRemovable = true, isArchive = false)
        listViewRepo.listViews = listOf(archiveList,archiveNonRemovableList,nonArchiveRemovableList)

        val result = getSelectableLists.invoke(true,1).first().map { it.listView }

        assertThat(result).contains(archiveList)
        assertThat(result).doesNotContain(archiveNonRemovableList)
        assertThat(result).contains(nonArchiveRemovableList)
    }

    @Test
    fun ifUseArchiveAsList_isFalse_returnsNonArchiveRemovableLists() = runTest {

        val archiveList = listView(isArchive = true, isRemovable = true)
        val archiveNonRemovableList = listView(isArchive = true, isRemovable = false)
        val nonArchiveRemovableList = listView(isRemovable = true, isArchive = false)
        listViewRepo.listViews = listOf(archiveList,archiveNonRemovableList,nonArchiveRemovableList)

        val result = getSelectableLists.invoke(false,1).first().map { it.listView }

        assertThat(result).doesNotContain(archiveList)
        assertThat(result).doesNotContain(archiveNonRemovableList)
        assertThat(result).contains(nonArchiveRemovableList)
    }

    @Test
    fun whenListsHaveWordId_returnsAsSelectedList() = runTest {
        val listView1 = listView(id = 1, isRemovable = true)
        val listView2 = listView(id = 2, isRemovable = true)

        val wordId = 2
        listViewRepo.listViews = listOf(listView1,listView2)
        listViewRepo.wordIdListViewsMap = mapOf(wordId to listOf(listView1))

        val result = getSelectableLists(null,wordId).first()
        val listViewResult1 = result.first { it.listView == listView1 }
        val listViewResult2 = result.first { it.listView == listView2 }

        assertThat(listViewResult1.isSelected).isTrue()
        assertThat(listViewResult2.isSelected).isFalse()

    }

}