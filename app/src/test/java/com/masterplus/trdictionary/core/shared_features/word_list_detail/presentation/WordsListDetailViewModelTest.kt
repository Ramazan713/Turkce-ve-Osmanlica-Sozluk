package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.repo.ListRepoFake
import com.masterplus.trdictionary.core.data.repo.ListViewRepoFake
import com.masterplus.trdictionary.core.data.repo.ListWordsRepoFake
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.repo.ListViewRepo
import com.masterplus.trdictionary.core.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import com.masterplus.trdictionary.core.domain.use_cases.list_words.AddFavoriteListWords
import com.masterplus.trdictionary.core.domain.use_cases.list_words.AddListWords
import com.masterplus.trdictionary.core.domain.use_cases.list_words.GetSelectableLists
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.core.shared_features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.TTSRepoFake
import com.masterplus.trdictionary.core.shared_features.word_list_detail.data.repo.WordDetailRepoFake
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.AudioState
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import com.masterplus.trdictionary.core.utils.sample_data.listView
import com.masterplus.trdictionary.core.utils.sample_data.wordDetail
import com.masterplus.trdictionary.core.utils.sample_data.wordDetailMeanings
import com.masterplus.trdictionary.core.utils.sample_data.wordWithSimilar
import com.masterplus.trdictionary.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class WordsListDetailViewModelTest {

    private lateinit var listRepo: ListRepoFake
    private lateinit var listWordsRepo: ListWordsRepoFake
    private lateinit var listViewRepo: ListViewRepoFake
    private lateinit var listWordsUseCases: ListWordsUseCases
    private lateinit var wordDetailRepo: WordDetailRepoFake


    private lateinit var ttsRepo: TTSRepoFake
    private lateinit var listInUseCase: ListInFavoriteControlForDeletionUseCase
    private lateinit var shareWordUseCases: ShareWordUseCases

    private lateinit var viewModel: WordsListDetailViewModel

    @BeforeEach
    fun setUp() {
        initViewModel()
    }


    @Test
    fun showSelectedWords_whenEvent_shouldChangeDetailPosAndDetailOpenState() = runTest {
        advanceUntilIdle()
        val initState = viewModel.state.first()
        val pos = 10

        viewModel.onEvent(WordsListDetailEvent.ShowSelectedWords(word = wordWithSimilar(), pos = pos))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.isDetailOpen).isFalse()
        assertThat(lastState.isDetailOpen).isTrue()
        assertThat(lastState.selectedDetailPos).isEqualTo(pos)
        assertThat(lastState.selectedDetailPos).isNotEqualTo(initState.selectedDetailPos)
    }

    @Test
    fun hideSelectedWords_whenEvent_shouldChangeListPosAndDetailOpenState() = runTest {
        viewModel.onEvent(WordsListDetailEvent.ShowSelectedWords(word = wordWithSimilar(), pos = 2))
        advanceUntilIdle()
        val initState = viewModel.state.first()
        val listPos = 10

        viewModel.onEvent(WordsListDetailEvent.HideSelectedWords(listPos))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.isDetailOpen).isTrue()
        assertThat(lastState.isDetailOpen).isFalse()
        assertThat(lastState.navigateToListPos).isEqualTo(listPos)
    }

    @ParameterizedTest
    @ValueSource(ints = [1,7,9,13,15])
    fun navigateToPos_whenEvent_shouldChangeToPos(destPos: Int) = runTest {
        viewModel.onEvent(WordsListDetailEvent.NavigateToPos(destPos))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(lastState.navigateToPos).isEqualTo(destPos)
    }

    @Test
    fun clearNavigateToPos_whenEvent_shouldClearAllPosRelatedField() = runTest {
        val navPos = 10
        val listPos = 7
        val detailPos = 5

        viewModel.onEvent(WordsListDetailEvent.ShowSelectedWords(word = wordWithSimilar(), pos = detailPos))
        viewModel.onEvent(WordsListDetailEvent.HideSelectedWords(listPos))
        viewModel.onEvent(WordsListDetailEvent.NavigateToPos(navPos))
        advanceUntilIdle()
        val initState = viewModel.state.first()

        viewModel.onEvent(WordsListDetailEvent.ClearNavigateToPos)
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.navigateToPos).isEqualTo(navPos)
        assertThat(initState.selectedDetailPos).isEqualTo(detailPos)
        assertThat(initState.navigateToListPos).isEqualTo(listPos)
        assertThat(lastState.navigateToPos).isNull()
        assertThat(lastState.selectedDetailPos).isNull()
        assertThat(lastState.navigateToListPos).isNull()
    }

    @Test
    fun addFavorite_whenControlListIdValidAndInFavoriteIsTrue_shouldShowAskFavoriteDeleteDialog() = runTest {
        val wordId = 2
        val listIdControl = 3
        listRepo.insertItem(listView(id = listIdControl))
        listRepo.favoriteListIds.add(listIdControl)

        advanceUntilIdle()
        val initState = viewModel.state.first()

        viewModel.onEvent(WordsListDetailEvent.AddFavorite(wordId = wordId, listIdControl = listIdControl, inFavorite = true))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(initState.dialogEvent).isNull()
        assertThat(lastState.dialogEvent).isEqualTo(WordsListDetailDialogEvent.AskFavoriteDelete(wordId))
    }

    @ParameterizedTest
    @CsvSource(
        "2,true,,true",
        "2,true,3,false",
        "2,false,3,true",
    )
    fun addFavorite_whenUseCaseReturnsFalse_shouldCalledAddFavoriteListWords(
        wordId: Int,
        setListIdControlInFavoriteList: Boolean,
        listIdControl: Int?,
        inFavorite: Boolean,
    ) = runTest {
        if(listIdControl != null){
            listRepo.insertItem(listView(id = listIdControl))
            if(setListIdControlInFavoriteList){
                listRepo.favoriteListIds.add(listIdControl)
            }
        }
        mockkObject(listWordsUseCases.addFavoriteListWords)

        viewModel.onEvent(WordsListDetailEvent.AddFavorite(wordId = wordId, listIdControl = listIdControl, inFavorite = inFavorite))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(lastState.dialogEvent).isNull()
        coVerify {
            listWordsUseCases.addFavoriteListWords(wordId)
        }
    }

    @Test
    fun addListWords_whenEvent_shouldCalledAddListWordsUseCase() = runTest {
        mockkObject(listWordsUseCases.addListWords)
        viewModel.onEvent(WordsListDetailEvent.AddListWords(listView(),1))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            listWordsUseCases.addListWords(listView(),1)
        }
    }

    @Test
    fun listenWords_whenEvent_shouldCalledSynthesizeAndPlay() = runTest {
        mockkObject(ttsRepo)
        val wordDetail = wordDetail(id = 3)

        viewModel.onEvent(WordsListDetailEvent.ListenWords(wordDetail))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            ttsRepo.synthesizeAndPlay(wordDetail.id.toString(),wordDetail.word)
        }
    }

    @Test
    fun listenWords_whenHasError_shouldChangeStateMessage() = runTest {
        val message = UiText.Text("error")
        ttsRepo.returnedSynthesizeAndPlay = Resource.Error(message)

        viewModel.onEvent(WordsListDetailEvent.ListenWords(wordDetail()))
        advanceUntilIdle()
        val state = viewModel.state.first()

        assertThat(state.message).isEqualTo(message)
    }

    @Test
    fun showDialog_whenEvent_shouldChangeStateDialog() = runTest {
        val dialogEvent = WordsListDetailDialogEvent.ShowCompoundWordsList(wordDetailMeanings())

        viewModel.onEvent(WordsListDetailEvent.ShowDialog(dialogEvent))
        advanceUntilIdle()
        val state = viewModel.state.first()

        assertThat(state.dialogEvent).isEqualTo(dialogEvent)
    }

    @Test
    fun showSheet_whenEvent_shouldChangeStateSheet() = runTest {
        val sheetEvent = WordsListDetailSheetEvent.ShowSelectList(1,null)

        viewModel.onEvent(WordsListDetailEvent.ShowSheet(sheetEvent))
        advanceUntilIdle()
        val state = viewModel.state.first()

        assertThat(state.sheetEvent).isEqualTo(sheetEvent)
    }

    @Test
    fun shareWord_whenEvent_shouldChangeStateShareResultEvent() = runTest {
        val shareItem = ShareItemEnum.ShareWord
        val wordDetail = wordDetail(word = "sample")

        viewModel.onEvent(WordsListDetailEvent.ShareWord(shareItem, wordDetail))
        advanceUntilIdle()
        val state = viewModel.state.first()

        assertThat(state.shareResultEvent).isEqualTo(ShareWordUseCases.ShareWordResult.ShareWord(wordDetail.word))
    }

    @Test
    fun clearShareResult_whenHasShareResultEvent_shouldSetNull() = runTest {
        viewModel.onEvent(WordsListDetailEvent.ShareWord(ShareItemEnum.ShareWord, wordDetail()))
        advanceUntilIdle()
        val firstState = viewModel.state.first()

        viewModel.onEvent((WordsListDetailEvent.ClearShareResult))
        advanceUntilIdle()
        val lastState = viewModel.state.first()

        assertThat(firstState.shareResultEvent).isNotNull()
        assertThat(lastState.shareResultEvent).isNull()
    }

    @Test
    fun listenTTSNetworkState_whenAudioStateChange_shouldChangeState() = runTest {
        val firstAudioState = AudioState(isPlaying = true, isVisible = true)
        val secondAudioState = AudioState(isPlaying = false, isVisible = false)

        initViewModel {
            ttsRepo.returnedAudioStateFlow = flow {
                emit(firstAudioState)
                emit(secondAudioState)
            }
        }
        viewModel.state.test {
            awaitItem()//first state
            val firstItem = awaitItem()
            val secondItem = awaitItem()

            assertThat(firstItem.audioState).isEqualTo(firstAudioState)
            assertThat(secondItem.audioState).isEqualTo(secondAudioState)
        }
    }


    private fun initViewModel(onBeforeInitViewModel: (() -> Unit)? = null ){
        wordDetailRepo = WordDetailRepoFake()
        listViewRepo = ListViewRepoFake()
        listRepo = ListRepoFake()
        listWordsRepo = ListWordsRepoFake()
        listWordsUseCases = ListWordsUseCases(
            getSelectableLists = GetSelectableLists(listViewRepo),
            addListWords = AddListWords(listWordsRepo),
            addFavoriteListWords = AddFavoriteListWords(listWordsRepo,listViewRepo, listRepo)
        )

        listInUseCase= ListInFavoriteControlForDeletionUseCase(listRepo)
        ttsRepo = TTSRepoFake()
        shareWordUseCases = ShareWordUseCases(wordDetailRepo)

        onBeforeInitViewModel?.invoke()

        viewModel = WordsListDetailViewModel(
            listInFavoriteControlForDeletionUseCase = listInUseCase,
            listWordsUseCases = listWordsUseCases,
            shareWordUseCases = shareWordUseCases,
            ttsRepo = ttsRepo
        )
    }
}