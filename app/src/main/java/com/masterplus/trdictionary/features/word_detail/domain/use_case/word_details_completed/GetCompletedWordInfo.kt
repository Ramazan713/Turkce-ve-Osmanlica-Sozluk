package com.masterplus.trdictionary.features.word_detail.domain.use_case.word_details_completed

import com.masterplus.trdictionary.features.word_detail.domain.model.*
import com.masterplus.trdictionary.features.word_detail.domain.repo.WordDetailRepo
import javax.inject.Inject

class GetCompletedWordInfo @Inject constructor(
    private val wordDetailRepo: WordDetailRepo
){

    suspend operator fun invoke(word: WordListInfoSimilarWords): WordCompletedInfo {
        val parentWordDetail = getWordDetailInfo(word.wordListInfo)
        val wordDetails = word.similarWords.mapNotNull {
            getWordDetailInfoFromWordId(it.id)
        }

        return WordCompletedInfo(wordInfo = parentWordDetail, similarWordsInfo = wordDetails)
    }

    private suspend fun getWordDetailInfoFromWordId(wordId: Int): WordDetailInfoModel?{
        val wordMeanings = wordDetailRepo.getWordMeaningsWithWordId(wordId) ?: return null

        val wordListInfo = WordListInfo(wordMeanings,false, inFavorite = false)

        return getWordDetailInfo(wordListInfo)
    }

    private suspend fun getWordDetailInfo(wordListInfo: WordListInfo): WordDetailInfoModel {
        val wordId = wordListInfo.wordMeaning.word.id
        val compoundWords = wordDetailRepo.getCompoundSimpleWordsByWordId(wordId)
        val proverbIdioms = wordDetailRepo.getProverbIdiomWordsBywordId(wordId)

        return WordDetailInfoModel(
            wordListInfo = wordListInfo,
            proverbIdioms = proverbIdioms,
            compoundWords = compoundWords
        )
    }
}