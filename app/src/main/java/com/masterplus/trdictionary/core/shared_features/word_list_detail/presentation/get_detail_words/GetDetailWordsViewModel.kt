package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.get_detail_words

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo.WordDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDetailWordsViewModel @Inject constructor(
    private val wordDetailRepo: WordDetailRepo
): ViewModel() {

    var proverbIdiomWords = mutableStateListOf<SimpleWordResult>()
        private set

    var compoundWords = mutableStateListOf<SimpleWordResult>()
        private set

    fun loadProverbIdiomWords(wordId: Int){
        viewModelScope.launch {
            val words = wordDetailRepo.getProverbIdiomWordsBywordId(wordId)
            proverbIdiomWords.clear()
            proverbIdiomWords.addAll(words)
        }
    }

    fun loadCompoundWords(wordId: Int){
        viewModelScope.launch {
            val words = wordDetailRepo.getCompoundSimpleWordsByWordId(wordId)
            compoundWords.clear()
            compoundWords.addAll(words)
        }
    }
}