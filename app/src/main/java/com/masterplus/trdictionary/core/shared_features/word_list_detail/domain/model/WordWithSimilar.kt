package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model

data class WordWithSimilar(
    val wordDetailMeanings: WordDetailMeanings,
    val similarWords: List<WordDetailMeanings>
){
    val wordDetail get() = wordDetailMeanings.wordDetail

    val wordId get() = wordDetail.id


    val allWords: List<WordDetailMeanings> get() {
        return mutableListOf<WordDetailMeanings>().apply {
            add(wordDetailMeanings)
            addAll(similarWords)
        }.toList()
    }
}
