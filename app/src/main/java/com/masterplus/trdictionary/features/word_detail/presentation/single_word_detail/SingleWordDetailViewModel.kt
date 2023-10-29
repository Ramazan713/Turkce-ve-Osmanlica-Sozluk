package com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.tts.TTSNetworkAudioUseCase
import com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.word_detail.presentation.single_word_detail.navigation.SingleWordDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleWordDetailViewModel @Inject constructor(
    private val wordDetailUseCases: WordDetailsCompletedUseCases,
    private val listWordsUseCases: ListWordsUseCases,
    private val ttsNetworkUseCase: TTSNetworkAudioUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val args = SingleWordDetailArgs(savedStateHandle)

    var state by mutableStateOf(SingleWordDetailState())
        private set

    private var loadDataJob: Job? = null

    init {
        listenTTSNetworkState()
        loadData(args.wordId)
    }

    fun onEvent(event: SingleWordDetailEvent){
        when(event){
            is SingleWordDetailEvent.AddFavorite -> {
                viewModelScope.launch {
                    listWordsUseCases.addFavoriteListWords(event.wordId)
                }
            }
            is SingleWordDetailEvent.AddListWord -> {
                viewModelScope.launch {
                    listWordsUseCases.addListWords(event.listView,event.wordId)
                }
            }
            is SingleWordDetailEvent.ShowModal -> {
                state = state.copy(showModal = event.showModal, modalEvent = event.modalEvent)
            }
            is SingleWordDetailEvent.ShowDialog -> {
                state = state.copy(showDialog = event.showDialog, dialogEvent = event.dialogEvent)
            }
            is SingleWordDetailEvent.ListenWord -> {
                viewModelScope.launch {
                    ttsNetworkUseCase(event.word.word)
                }
            }
        }
    }

    private fun loadData(wordId: Int){
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            wordDetailUseCases.getCompletedWordFlow(wordId).collectLatest { word->
                state = state.copy(allWords = word?.allWords ?: emptyList())
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