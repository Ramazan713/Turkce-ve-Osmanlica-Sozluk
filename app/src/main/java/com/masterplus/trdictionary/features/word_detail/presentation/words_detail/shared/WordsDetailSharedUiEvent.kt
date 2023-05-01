package com.masterplus.trdictionary.features.word_detail.presentation.words_detail.shared

import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases

sealed class WordsDetailSharedUiEvent{
    data class ShareWord(val shareResult: ShareWordUseCases.ShareWordResult): WordsDetailSharedUiEvent()
}
