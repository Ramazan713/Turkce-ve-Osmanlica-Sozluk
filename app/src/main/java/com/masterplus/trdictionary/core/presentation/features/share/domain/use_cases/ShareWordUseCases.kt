package com.masterplus.trdictionary.core.presentation.features.share.domain.use_cases

import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.core.presentation.features.share.domain.enums.ShareItemEnum
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo

data class ShareWordUseCases(
    private val wordDetailRepo: WordDetailRepo,
){

    suspend operator fun invoke(
        word: Word,
        shareItemEnum: ShareItemEnum,
    ): ShareWordResult {
        return when(shareItemEnum){
            ShareItemEnum.ShareWord -> {
                ShareWordResult.ShareWord(word.word)
            }
            ShareItemEnum.ShareWordMeaning -> {
                val wordMeanings = wordDetailRepo.getWordMeaningsWithWordId(word.id)
                ShareWordResult.ShareWordWithMeanings(wordMeanings)
            }
            ShareItemEnum.CopyWord -> {
                ShareWordResult.CopyWord(word.word)
            }
        }
    }



    sealed class ShareWordResult {

        data class ShareWord(val wordText: String): ShareWordResult()

        data class ShareWordWithMeanings(val wordMeanings: WordDetailMeanings?): ShareWordResult()

        data class CopyWord(val wordText: String): ShareWordResult()
    }
}
