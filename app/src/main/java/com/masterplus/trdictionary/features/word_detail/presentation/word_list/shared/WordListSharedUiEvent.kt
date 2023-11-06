package com.masterplus.trdictionary.features.word_detail.presentation.word_list.shared

import com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases.ShareWordUseCases

sealed class WordListSharedUiEvent{
    data class ShareWord(val shareResult: ShareWordUseCases.ShareWordResult): WordListSharedUiEvent()
}
