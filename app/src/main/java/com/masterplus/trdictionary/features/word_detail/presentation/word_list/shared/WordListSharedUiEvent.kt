package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.features.word_detail.domain.use_case.share.ShareWordUseCases

sealed class WordListSharedUiEvent{
    data class ShareWord(val shareResult: ShareWordUseCases.ShareWordResult): WordListSharedUiEvent()
}
