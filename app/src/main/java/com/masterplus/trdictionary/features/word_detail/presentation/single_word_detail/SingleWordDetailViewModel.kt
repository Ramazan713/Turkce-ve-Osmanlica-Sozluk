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
    wordDetailUseCases: WordDetailsCompletedUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val args = SingleWordDetailArgs(savedStateHandle)

    val word = wordDetailUseCases.getCompletedWordFlow(args.wordId)
}