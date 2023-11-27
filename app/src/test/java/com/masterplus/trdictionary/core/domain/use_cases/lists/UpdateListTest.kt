package com.masterplus.trdictionary.core.domain.use_cases.lists

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.utils.sample_data.listModel
import com.masterplus.trdictionary.core.utils.sample_data.listView
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateListTest {

    private lateinit var listRepo: ListRepoFake
    private lateinit var updateList: UpdateList

    @BeforeEach
    fun setUp() {
        listRepo = ListRepoFake()
        updateList = UpdateList(listRepo)
    }

    @Test
    fun updateOnly_listName_success() = runTest {

        var listView = listView(isArchive = false)
        listRepo.insertList(listView.toListModel()).toInt().also {
            listView = listView.copy(id = it)
        }

        updateList.invoke(listView,"updatedName")

        val updatedModel = listRepo.getListById(listView.id!!)

        assertThat(updatedModel?.name).isEqualTo("updatedName")
        assertThat(updatedModel?.isArchive).isEqualTo(false)
    }

    @Test
    fun updateOnly_IsArchive_success() = runTest {

        var listView = listView(isArchive = true)
        listRepo.insertList(listView.toListModel()).toInt().also {
            listView = listView.copy(id = it)
        }

        updateList.invoke(listView, newIsArchive = false)

        val updatedModel = listRepo.getListById(listView.id!!)

        assertThat(updatedModel?.isArchive).isEqualTo(false)
    }


}