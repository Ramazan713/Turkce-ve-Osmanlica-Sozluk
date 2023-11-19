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
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.TTSRepo
import com.masterplus.trdictionary.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsListDetailViewModel @Inject constructor(
    private val listWordsUseCases: ListWordsUseCases,
    private val ttsRepo: TTSRepo,
    private val shareWordUseCases: ShareWordUseCases,
    private val listInFavoriteControlForDeletionUseCase: ListInFavoriteControlForDeletionUseCase
): ViewModel() {


    private val _state = MutableStateFlow(WordsListDetailState())
    val state: StateFlow<WordsListDetailState> = _state.asStateFlow()

    init {
        listenTTSNetworkState()
    }

    fun onEvent(event: WordsListDetailEvent){
        when(event){
            WordsListDetailEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
            is WordsListDetailEvent.HideSelectedWords -> {
                _state.update { state-> state.copy(
                    isDetailOpen = false,
                    navigateToListPos = event.lastPos
                )}
            }
            is WordsListDetailEvent.ShowSelectedWords -> {
                _state.update { state-> state.copy(
                    selectedDetailPos = event.pos,
                    isDetailOpen = true
                )}
            }
            is WordsListDetailEvent.NavigateToPos -> {
                _state.update { it.copy(navigateToPos = event.pos)}
            }
            WordsListDetailEvent.ClearNavigateToPos -> {
                _state.update { state-> state.copy(
                    navigateToPos = null,
                    selectedDetailPos = null,
                    navigateToListPos = null
                )}
            }
            is WordsListDetailEvent.AddFavorite -> {
                viewModelScope.launch {
                    listInFavoriteControlForDeletionUseCase(event.listIdControl,event.inFavorite).let { showDia->
                        if(showDia){
                            _state.update { state-> state.copy(
                                dialogEvent = WordsListDetailDialogEvent.AskFavoriteDelete(event.wordId)
                            )}
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
                    val result = ttsRepo.synthesizeAndPlay(
                        key = event.word.id.toString(),
                        text = event.word.word
                    )
                    if(result is Resource.Error){
                        _state.update { it.copy(message = result.error)}
                    }
                }
            }
            is WordsListDetailEvent.ShowDialog -> {
                _state.update { it.copy(dialogEvent = event.dialogEvent)}
            }
            is WordsListDetailEvent.ShowSheet -> {
                _state.update { it.copy(sheetEvent = event.sheetEvent)}
            }

            WordsListDetailEvent.ClearShareResult -> {
                _state.update { it.copy(shareResultEvent = null)}
            }
            is WordsListDetailEvent.ShareWord -> {
                viewModelScope.launch {
                    val result = shareWordUseCases.invoke(
                        word = event.wordDetail.toWord(),
                        shareItemEnum = event.shareItem
                    )
                    _state.update { it.copy(shareResultEvent = result)}
                }
            }
        }
    }

    private fun listenTTSNetworkState(){
        viewModelScope.launch {
            ttsRepo.audioState.collectLatest {audioState->
                _state.update { it.copy(audioState = audioState)}
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsRepo.dispose()
    }
}