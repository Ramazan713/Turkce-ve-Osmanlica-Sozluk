package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases

sealed class WordsDetailSharedUiEvent{
    data class ShareWord(val shareResult: ShareWordUseCases.ShareWordResult): WordsDetailSharedUiEvent()
}
