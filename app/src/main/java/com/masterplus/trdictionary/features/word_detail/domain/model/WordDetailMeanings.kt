package com.masterplus.trdictionary.features.word_detail.domain.model

data class WordDetailMeanings(
    val wordDetail: WordDetail,
    val meanings: List<MeaningExamples>
){
    val wordId get() = wordDetail.id
}
