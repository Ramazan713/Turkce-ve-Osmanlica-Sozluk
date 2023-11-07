package com.masterplus.trdictionary.features.word_detail.single_word_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.use_case.word_details_completed.WordDetailsCompletedUseCases
import com.masterplus.trdictionary.features.word_detail.single_word_detail.navigation.SingleWordDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleWordDetailViewModel @Inject constructor(
    wordDetailUseCases: WordDetailsCompletedUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val args = SingleWordDetailArgs(savedStateHandle)

    val word = wordDetailUseCases.getCompletedWordFlow(args.wordId)
}