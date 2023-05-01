package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.repo.ListRepo
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.tts.TTSNetworkAudioUseCase
import com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordDetailListDialogEvent
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedEvent
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedState
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedUiEvent
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_list.navigation.WordsDetailListArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WordsDetailListViewModel @Inject constructor(
    wordDetailsCompletedUseCases: WordDetailsCompletedUseCases,
    private val listWordsUseCases: ListWordsUseCases,
    private val listRepo: ListRepo,
    private val ttsNetworkUseCase: TTSNetworkAudioUseCase,
    private val shareWordUseCases: ShareWordUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val args = WordsDetailListArgs(savedStateHandle)

    val pagingWords = wordDetailsCompletedUseCases.getListCompletedWordsPaging(args.listId)
        .cachedIn(viewModelScope)

    var sharedState by mutableStateOf(WordsDetailSharedState())
        private set

    var state by mutableStateOf(WordsDetailListState())
        private set

    init {
        loadData()
        listenTTSNetworkState()
    }

    fun onEvent(event: WordsDetailListEvent){
        when(event){
            is WordsDetailListEvent.AddOrAskFavorite -> {
                viewModelScope.launch {
                    state.listModel?.id?.let { listId->
                        if(listRepo.isFavoriteList(listId)){
                            sharedState = sharedState.copy(
                                showDialog = true,
                                dialogEvent = WordDetailListDialogEvent.AskUnFavorite(event.wordId)
                            )
                        }else{
                            listWordsUseCases.addFavoriteListWords(event.wordId)
                        }
                    }
                }
            }
        }
    }

    fun onSharedEvent(event: WordsDetailSharedEvent){
        when(event){
            is WordsDetailSharedEvent.ShowDialog -> {
                sharedState = sharedState.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)
            }
            is WordsDetailSharedEvent.ShowModal -> {
                sharedState = sharedState.copy(showModal = event.showModal, modalEvent = event.modalEvent)
            }
            is WordsDetailSharedEvent.AddFavorite -> {
                viewModelScope.launch {
                    listWordsUseCases.addFavoriteListWords(event.wordId)
                }
            }
            is WordsDetailSharedEvent.AddListWord -> {
                viewModelScope.launch {
                    listWordsUseCases.addListWords(event.listView,event.wordId)
                }
            }
            is WordsDetailSharedEvent.ListenWord -> {
                viewModelScope.launch {
                    state = state.copy(audioState = state.audioState.copy(isPlaying = true))
                    ttsNetworkUseCase(event.word.word)
                }
            }
            is WordsDetailSharedEvent.EvaluateShareWord -> {
                viewModelScope.launch {
                    val shareResult = shareWordUseCases.shareWordList(
                        savePointDestination = event.savePointDestination,
                        savePointType = event.savePointType,
                        pos = event.pos,
                        wordId = event.wordId,
                        wordRandomOrder = event.wordRandomOrder,
                        shareItemEnum = event.shareItemEnum,
                        urlBase = K.DeepLink.categoryDetailBaseUrl)
                    sharedState = sharedState.copy(uiEvent = WordsDetailSharedUiEvent.ShareWord(shareResult))
                }
            }
            WordsDetailSharedEvent.ClearUiEvent -> {
                sharedState = sharedState.copy(uiEvent = null)
            }
        }
    }

    private fun loadData(){
        viewModelScope.launch {
            val listModel = listRepo.getListById(args.listId)
            val savePointDestination = SavePointDestination.List(args.listId)
            state = state.copy(listModel = listModel, savePointDestination = savePointDestination)
        }
    }

    private fun listenTTSNetworkState(){
        viewModelScope.launch {
            ttsNetworkUseCase.audioState.collectLatest {audioState->
                state = state.copy(audioState = audioState)
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        ttsNetworkUseCase.dispose()
    }

}