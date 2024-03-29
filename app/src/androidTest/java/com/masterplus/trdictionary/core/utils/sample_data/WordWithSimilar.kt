package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.data.local.entities.relations.WordMeaningsRelation
import com.masterplus.trdictionary.core.data.local.entities.relations.WordWithSimilarRelation
import com.masterplus.trdictionary.core.data.local.views.WordDetailView
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilar

fun wordWithSimilar(
    wordId: Int = 1,
    wordType: WordType = WordType.PureWord,
    dictType: DictType = DictType.TR,
    word: String = "word $wordId",
    wordDetailMeanings: WordDetailMeanings = wordDetailMeanings(
        wordDetail = wordDetail(
            id = wordId,
            word = word,
            wordType = wordType,
            dictType = dictType
        )
    ),
    similarWords: List<WordDetailMeanings> = listOf(
        wordDetailMeanings(wordId = 10),
        wordDetailMeanings(wordId = 11)
    )
): WordWithSimilar {
    return WordWithSimilar(
        wordDetailMeanings, similarWords
    )
}


fun wordWithSimilarRelation(
    wordId: Int = 1,
    wordType: WordType = WordType.PureWord,
    dictType: DictType = DictType.TR,
    word: String = "word $wordId",
    wordDetailMeanings: WordMeaningsRelation = wordDetailMeaningsRelation(
        wordDetail = wordDetailView(
            id = wordId,
            word = word,
            wordType = wordType,
            dictType = dictType
        )
    ),
    similarWords: List<WordDetailView> = listOf(
        wordDetailView(id = 10),
        wordDetailView(id = 11)
    )
): WordWithSimilarRelation {
    return WordWithSimilarRelation(
        wordDetailMeanings, similarWords
    )
}