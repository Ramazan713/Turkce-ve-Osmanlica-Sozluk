package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDetailWordsViewModel @Inject constructor(
    private val wordDetailRepo: WordDetailRepo
): ViewModel() {

    private val _state = MutableStateFlow(DetailWordsState())
    val state: StateFlow<DetailWordsState> = _state.asStateFlow()

    fun loadProverbIdiomWords(wordId: Int){
        viewModelScope.launch {
            val words = wordDetailRepo.getProverbIdiomWordsBywordId(wordId)
            _state.update { it.copy(proverbIdiomWords = words) }
        }
    }

    fun loadCompoundWords(wordId: Int){
        viewModelScope.launch {
            val words = wordDetailRepo.getCompoundSimpleWordsByWordId(wordId)
            _state.update { it.copy(compoundWords = words) }
        }
    }
}