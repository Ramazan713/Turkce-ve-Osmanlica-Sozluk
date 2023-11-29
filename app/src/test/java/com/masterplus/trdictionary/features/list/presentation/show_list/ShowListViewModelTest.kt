package com.masterplus.trdictionary.features.list.presentation.show_list

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.data.local.TransactionProviderFake
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.data.repo.ListViewRepoFake
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.data.repo.SavePointRepoFake
import com.masterplus.trdictionary.core.domain.use_cases.lists.CopyList
import com.masterplus.trdictionary.core.domain.use_cases.lists.DeleteList
import com.masterplus.trdictionary.core.domain.use_cases.lists.GetLists
import com.masterplus.trdictionary.core.domain.use_cases.lists.InsertList
import com.masterplus.trdictionary.core.domain.use_cases.lists.ListUseCases
import com.masterplus.trdictionary.core.domain.use_cases.lists.UpdateList
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class ShowListViewModelTest {

    private lateinit var listRepo: ListRepoFake
    private lateinit var listUseCases: ListUseCases
    private lateinit var showListViewModel: ShowListViewModel
    @BeforeEach
    fun setUp() {
        listRepo = ListRepoFake()
        listUseCases = ListUseCases(
            insertList = InsertList(listRepo),
            updateList = UpdateList(listRepo),
            deleteList = DeleteList(listRepo,ListWordsRepoFake(),SavePointRepoFake(),TransactionProviderFake()),
            getLists = GetLists(ListViewRepoFake()),
            copyList = CopyList(listRepo,ListWordsRepoFake())
        )

        showListViewModel = ShowListViewModel(listUseCases)
    }


    @Test
    fun ifShowDialogSet_shouldStateChange() = runTest {
        val firstState = showListViewModel.state.value
        val dialogEvent = ShowListDialogEvent.TitleToAddList

        showListViewModel.onEvent(ShowListEvent.ShowDialog(true,dialogEvent))
        advanceUntilIdle()

        val lastState = showListViewModel.state.value

        assertThat(firstState.showDialog).isFalse()
        assertThat(firstState.dialogEvent).isNull()
        assertThat(lastState.showDialog).isTrue()
        assertThat(lastState.dialogEvent).isEqualTo(dialogEvent)
    }

    @Test
    fun ifClearMessageEvent_shouldClearMessage() = runTest {
        showListViewModel.state.test {
            awaitItem()
            showListViewModel.onEvent(ShowListEvent.AddNewList("test"))
            val firstState = awaitItem()

            showListViewModel.onEvent(ShowListEvent.ClearMessage)
            val lastState = awaitItem()

            assertThat(firstState.message).isEqualTo(UiText.Resource(R.string.successfully_added))
            assertThat(lastState.message).isNull()
        }
    }

    @Test
    fun whenNewListAdded_shouldListAdded() = runTest {
        showListViewModel.state.test {
            val firstState = awaitItem()

            val firstListItems = listRepo.items.toList()

            showListViewModel.onEvent(ShowListEvent.AddNewList("test"))
            val lastState = awaitItem()
            val lastListItems = listRepo.items.toList()

            assertThat(firstState.message).isNull()
            assertThat(lastState.message).isNotNull()
            assertThat(firstListItems.size).isEqualTo(0)
            assertThat(lastListItems.size).isEqualTo(1)
        }
    }


}