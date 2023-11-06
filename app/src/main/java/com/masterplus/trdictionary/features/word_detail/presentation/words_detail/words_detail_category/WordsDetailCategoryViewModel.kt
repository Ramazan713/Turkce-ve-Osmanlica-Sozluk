package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.words_detail_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.use_cases.list_words.ListWordsUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.save_point_info.SavePointCategoryInfoUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.use_case.tts.TTSNetworkAudioUseCase
import com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedEvent
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedState
import com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared.WordsDetailSharedUiEvent
import com.masterplus.trdictionary.features.word_detail.presentation.word_detail_list.word_category.navigation.WordCategoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WordsDetailCategoryViewModel @Inject constructor(
    wordDetailUseCases: WordDetailsCompletedUseCases,
    private val listWordsUseCases: ListWordsUseCases,
    private val savePointCategoryInfoUseCases: SavePointCategoryInfoUseCases,
    private val ttsNetworkUseCase: TTSNetworkAudioUseCase,
    private val shareWordUseCases: ShareWordUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val args = WordCategoryArgs(savedStateHandle)

    val pagingWords = wordDetailUseCases.getCategoryCompletedWordsPaging(args.cat,args.subCat,args.c)
        .cachedIn(viewModelScope)

    var sharedState by mutableStateOf(WordsDetailSharedState())
        private set

    var state by mutableStateOf(WordsDetailCategoryState())
        private set

    init {
        loadData()
        listenTTSNetworkState()
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
             is WordsDetailSharedEvent.ClearUiEvent -> {
                sharedState = sharedState.copy(uiEvent = null)
             }
         }
    }

    private fun loadData(){
        viewModelScope.launch {
            savePointCategoryInfoUseCases(args.catId,args.subCatId,args.c).let { result->
                state = state.copy(
                    catEnum = result.catEnum,
                    subCategoryEnum = result.subCategoryEnum,
                    savePointTitle = result.savePointTitle,
                    savePointDestination = result.savePointDestination
                )
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