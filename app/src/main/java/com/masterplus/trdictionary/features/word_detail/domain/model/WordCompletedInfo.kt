package com.masterplus.trdictionary.features.word_detail.domain.model

data class WordCompletedInfo(
    val wordInfo: WordDetailInfoModel,
    val similarWordsInfo: List<WordDetailInfoModel>
){
    val allWordInfos get() = mutableListOf<WordDetailInfoModel>().apply {
        add(wordInfo)
        addAll(similarWordsInfo)
    }.toList()
}
