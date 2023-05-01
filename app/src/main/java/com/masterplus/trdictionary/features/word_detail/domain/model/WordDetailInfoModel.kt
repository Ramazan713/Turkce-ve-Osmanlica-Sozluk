package com.masterplus.trdictionary.features.word_detail.domain.model

import com.masterplus.trdictionary.core.domain.model.SimpleWordResult

data class WordDetailInfoModel(
    val wordListInfo: WordListInfo,
    val proverbIdioms: List<SimpleWordResult>,
    val compoundWords: List<SimpleWordResult>,
){
    val wordId: Int get() = wordListInfo.wordMeaning.word.id

    val meanings get() = wordListInfo.wordMeaning.meanings

    val inFavorite get() = wordListInfo.inFavorite

    val inAnyList get() = wordListInfo.inAnyList

    val word get() = wordListInfo.wordMeaning.word
}
