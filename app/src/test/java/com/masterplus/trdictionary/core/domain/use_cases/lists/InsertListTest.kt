package com.masterplus.trdictionary.core.domain.use_cases.lists

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isSameAs
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.utils.sample_data.listModel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InsertListTest {

    private lateinit var listRepo: ListRepoFake
    private lateinit var insertList: InsertList

    @BeforeEach
    fun setUp() {
        listRepo = ListRepoFake()
        insertList = InsertList(listRepo)
    }


    @Test
    fun insert_success() = runTest {
        val listName = "myList"
        val listId = insertList.invoke(listName)

        val model = listRepo.getListById(listId)

        assertThat(model).isNotNull()
        assertThat(model?.name).isEqualTo(listName)
    }


    @Test
    fun insert_withDefaultParams_success() = runTest {
        val listId = insertList.invoke("listName")
        val model = listRepo.getListById(listId)

        assertThat(model?.isArchive).isSameAs(false)
        assertThat(model?.isRemovable).isSameAs(true)
    }

    @Test
    fun insert_increasePosition_success() = runTest {
        insertList.invoke("listName")

        val listId = insertList.invoke("listName2")
        val model = listRepo.getListById(listId)

        assertThat(model?.pos).isEqualTo(2)
    }


}