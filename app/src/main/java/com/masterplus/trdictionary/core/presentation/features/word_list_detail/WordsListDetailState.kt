package com.masterplus.trdictionary.core.presentation.features.word_list_detail

import androidx.paging.PagingData
import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases
import com.masterplus.trdictionary.features.word_detail.domain.model.AudioState
import com.masterplus.trdictionary.features.word_detail.domain.model.WordWithSimilar

data class WordsListDetailState(
    val message: String? = null,
    val isDetailOpen: Boolean = false,
    val words: PagingData<WordWithSimilar> = PagingData.empty(),
    val navigateToPos: Int? = null,
    val selectedDetailPos: Int? = null,
    val navigateToListPos: Int? = null,
    val audioState: AudioState = AudioState(),
    val dialogEvent: WordsListDetailDialogEvent? = null,
    val sheetEvent: WordsListDetailSheetEvent? = null,
    val shareResultEvent: ShareWordUseCases.ShareWordResult? = null
)
