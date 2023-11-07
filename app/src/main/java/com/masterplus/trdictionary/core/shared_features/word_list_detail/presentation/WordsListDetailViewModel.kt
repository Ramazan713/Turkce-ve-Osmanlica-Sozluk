package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.data.local.mapper.toWord
import com.masterplus.trdictionary.core.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.core.shared_features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.tts.TTSNetworkAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsListDetailViewModel @Inject constructor(
    private val listWordsUseCases: ListWordsUseCases,
    private val ttsNetworkUseCase: TTSNetworkAudioUseCase,
    private val shareWordUseCases: ShareWordUseCases,
    private val listInFavoriteControlForDeletionUseCase: ListInFavoriteControlForDeletionUseCase
): ViewModel() {

    var state by mutableStateOf(WordsListDetailState())
        private set

    init {
        listenTTSNetworkState()
    }

    fun onEvent(event: WordsListDetailEvent){
        when(event){
            WordsListDetailEvent.ClearMessage -> {
                state = state.copy(message = null)
            }
            is WordsListDetailEvent.HideSelectedWords -> {
                state = state.copy(
                    isDetailOpen = false,
                    navigateToListPos = event.lastPos
                )
            }
            is WordsListDetailEvent.ShowSelectedWords -> {
                state = state.copy(
                    selectedDetailPos = event.pos,
                    isDetailOpen = true
                )
            }
            is WordsListDetailEvent.NavigateToPos -> {
                state = state.copy(navigateToPos = event.pos)
            }
            WordsListDetailEvent.ClearNavigateToPos -> {
                state = state.copy(
                    navigateToPos = null,
                    selectedDetailPos = null,
                    navigateToListPos = null
                )
            }
            is WordsListDetailEvent.AddFavorite -> {
                viewModelScope.launch {
                    listInFavoriteControlForDeletionUseCase(event.listIdControl,event.inFavorite).let { showDia->
                        if(showDia){
                            state = state.copy(
                                dialogEvent = WordsListDetailDialogEvent.AskFavoriteDelete(event.wordId)
                            )
                        }else{
                            listWordsUseCases.addFavoriteListWords(event.wordId)
                        }
                    }
                }
            }
            is WordsListDetailEvent.AddListWords -> {
                viewModelScope.launch {
                    listWordsUseCases.addListWords(event.listView,event.wordId)
                }
            }
            is WordsListDetailEvent.ListenWords -> {
                viewModelScope.launch {
                    ttsNetworkUseCase(event.word.word)
                }
            }
            is WordsListDetailEvent.ShowDialog -> {
                state = state.copy(
                    dialogEvent = event.dialogEvent
                )
            }
            is WordsListDetailEvent.ShowSheet -> {
                state = state.copy(
                    sheetEvent = event.sheetEvent
                )
            }

            WordsListDetailEvent.ClearShareResult -> {
                state = state.copy(
                    shareResultEvent = null
                )
            }
            is WordsListDetailEvent.ShareWord -> {
                viewModelScope.launch {
                    val result = shareWordUseCases.invoke(
                        word = event.wordDetail.toWord(),
                        shareItemEnum = event.shareItem
                    )
                    state = state.copy(shareResultEvent = result)
                }
            }
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