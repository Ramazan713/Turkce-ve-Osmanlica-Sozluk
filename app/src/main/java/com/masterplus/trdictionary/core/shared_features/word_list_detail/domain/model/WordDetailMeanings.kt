package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model

data class WordDetailMeanings(
    val wordDetail: WordDetail,
    val meanings: List<MeaningExamples>
){
    val wordId get() = wordDetail.id
}
