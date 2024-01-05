package com.masterplus.trdictionary.features.home.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.utils.sample_data.shortInfoCollectionResult
import com.masterplus.trdictionary.features.home.data.manager.ShortInfoManagerFake
import com.masterplus.trdictionary.features.home.domain.enums.ShortInfoEnum
import com.masterplus.trdictionary.features.home.domain.manager.ShortInfoManager
import com.masterplus.trdictionary.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class HomeViewModelTest {

    private lateinit var manager: ShortInfoManagerFake
    private lateinit var homeViewModel: HomeViewModel


    @BeforeEach
    fun setUp() {
        manager = ShortInfoManagerFake()
        homeViewModel = HomeViewModel(manager)
    }


    @Test
    fun initLoading_whenViewModelStarts_shouldCheckDayForRefresh() = runTest {
        mockkObject(manager)
        homeViewModel = HomeViewModel(manager)

        advanceUntilIdle()
        coVerify(exactly = 1) {
            manager.checkDayForRefresh()
        }
    }

    @Test
    fun refreshShortInfo_whenAction_shouldRefreshWordCalled() = runTest {
        mockkObject(manager)
        val info = homeViewModel.state.first().proverbShortInfo

        homeViewModel.onEvent(HomeEvent.RefreshShortInfo(info))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            manager.refreshWord(info.shortInfo)
        }
    }


    @Test
    fun refreshShortInfo_whenProverbAction_shouldProverbStateChange() = runTest {
        homeViewModel.state.test {
            awaitItem()
            val firstState = awaitItem()
            homeViewModel.onEvent(HomeEvent.RefreshShortInfo(firstState.proverbShortInfo))

            val loadingState = awaitItem()
            val updatedState = awaitItem()

            assertThat(loadingState.proverbShortInfo.isLoading).isTrue()
            assertThat(updatedState.proverbShortInfo.isLoading).isFalse()
            assertThat(updatedState.proverbShortInfo.simpleWord).isNotEqualTo(firstState.proverbShortInfo.simpleWord)
        }
    }

    @Test
    fun refreshShortInfo_whenIdiomAction_shouldIdiomStateChange() = runTest {
        homeViewModel.state.test {
            awaitItem()
            val firstState = awaitItem()
            homeViewModel.onEvent(HomeEvent.RefreshShortInfo(firstState.idiomShortInfo))

            val loadingState = awaitItem()
            val updatedState = awaitItem()

            assertThat(loadingState.idiomShortInfo.isLoading).isTrue()
            assertThat(updatedState.idiomShortInfo.isLoading).isFalse()
            assertThat(updatedState.idiomShortInfo.simpleWord).isNotEqualTo(firstState.idiomShortInfo.simpleWord)
        }
    }

    @Test
    fun refreshShortInfo_whenWordAction_shouldWordStateChange() = runTest {
        homeViewModel.state.test {
            awaitItem()
            val firstState = awaitItem()
            homeViewModel.onEvent(HomeEvent.RefreshShortInfo(firstState.wordShortInfo))

            val loadingState = awaitItem()
            val updatedState = awaitItem()

            assertThat(loadingState.wordShortInfo.isLoading).isTrue()
            assertThat(updatedState.wordShortInfo.isLoading).isFalse()
            assertThat(updatedState.wordShortInfo.simpleWord).isNotEqualTo(firstState.wordShortInfo.simpleWord)
        }
    }


    @Test
    fun checkDayForRefresh_whenCheckDayForRefreshIsTrue_shouldItemsRefreshed() = runTest {
        manager.returnedCheckDayForRefresh = true
        val initCollection = shortInfoCollectionResult()
        manager.initCollection = initCollection

        homeViewModel = HomeViewModel(manager)
        advanceUntilIdle()

        val state = homeViewModel.state.value

        assertThat(state.proverbShortInfo.simpleWord).isNotEqualTo(initCollection.proverb)
        assertThat(state.idiomShortInfo.simpleWord).isNotEqualTo(initCollection.idiom)
        assertThat(state.wordShortInfo.simpleWord).isNotEqualTo(initCollection.word)
    }

    @Test
    fun checkDayForRefresh_whenCheckDayForRefreshIsFalse_shouldBeItemsSame() = runTest {
        manager.returnedCheckDayForRefresh = false
        val initCollection = shortInfoCollectionResult()
        manager.initCollection = initCollection

        homeViewModel = HomeViewModel(manager)
        advanceUntilIdle()

        val state = homeViewModel.state.value

        assertThat(state.proverbShortInfo.simpleWord).isEqualTo(initCollection.proverb)
        assertThat(state.idiomShortInfo.simpleWord).isEqualTo(initCollection.idiom)
        assertThat(state.wordShortInfo.simpleWord).isEqualTo(initCollection.word)
    }

}