package com.masterplus.trdictionary.features.word_detail.domain.use_case.share

import com.masterplus.trdictionary.features.word_detail.domain.model.WordMeanings

data class ShareWordUseCases(
    val shareWordList: ShareWordList,
){

    sealed class ShareWordResult{

        data class ShareLink(val link: String): ShareWordResult()

        data class ShareWord(val wordMeanings: WordMeanings?): ShareWordResult()

        data class ShareWordWithMeanings(val wordMeanings: WordMeanings?): ShareWordResult()
    }
}
