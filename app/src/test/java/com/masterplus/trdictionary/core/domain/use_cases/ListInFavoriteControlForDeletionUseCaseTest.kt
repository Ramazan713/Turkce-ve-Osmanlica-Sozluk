package com.masterplus.trdictionary.core.domain.use_cases

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.local.mapper.toListModel
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.domain.model.ListModel
import com.masterplus.trdictionary.core.domain.model.ListView
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.utils.sample_data.listView
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListInFavoriteControlForDeletionUseCaseTest {

    private lateinit var listRepo: ListRepoFake
    private lateinit var useCase: ListInFavoriteControlForDeletionUseCase

    @BeforeEach
    fun setUp() {
        listRepo = ListRepoFake()
        useCase = ListInFavoriteControlForDeletionUseCase(listRepo)
    }


    @Test
    fun whenParamsNotNullAndItemInFavList_returnTrue() = runTest {
        val listId = addFavoriteList()
        val result = useCase(listId,true)
        assertThat(result).isTrue()
    }

    @Test
    fun whenParamsNotNullAndItemNotInFavList_returnTrue() = runTest {
        val listId = addFavoriteList(addFavorite = false)
        val result = useCase(listId,true)
        assertThat(result).isFalse()
    }

    @Test
    fun whenListIdIsNull_returnFalse() = runTest {
        val result = useCase(null,true)
        assertThat(result).isFalse()
    }

    @Test
    fun whenIsInFavoriteIsFalse_returnFalse() = runTest {
        val listId = addFavoriteList(addFavorite = true)
        val result = useCase(listId,false)
        assertThat(result).isFalse()
    }


    private suspend fun addFavoriteList(listView: ListView = listView(),addFavorite: Boolean = true): Int{
        val id = listRepo.insertList(listView.toListModel())
        if(addFavorite){
            listRepo.favoriteListIds.add(id.toInt())
        }
        return id.toInt()
    }

}