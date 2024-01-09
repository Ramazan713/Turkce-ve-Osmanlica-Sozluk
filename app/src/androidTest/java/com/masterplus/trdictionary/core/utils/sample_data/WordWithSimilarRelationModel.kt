package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetailMeanings
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordWithSimilarRelationModel


fun wordWithSimilarRelationModel(
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
    similarWords: List<WordDetail> = listOf(
        wordDetail(id = 10),
        wordDetail(id = 11)
    )
): WordWithSimilarRelationModel{
    return WordWithSimilarRelationModel(
        wordDetailMeanings, similarWords
    )
}