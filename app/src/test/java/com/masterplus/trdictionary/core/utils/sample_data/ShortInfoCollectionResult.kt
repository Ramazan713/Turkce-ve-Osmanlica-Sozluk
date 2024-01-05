package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.features.home.domain.models.ShortInfoCollectionResult

fun shortInfoCollectionResult(
    word: SimpleWordResult = simpleWordResult(wordId = 1, wordType = WordType.PureWord),
    idiom: SimpleWordResult = simpleWordResult(wordId = 2, wordType = WordType.Idiom),
    proverb: SimpleWordResult = simpleWordResult(wordId = 3, wordType = WordType.Proverb)
): ShortInfoCollectionResult{
    return ShortInfoCollectionResult(
        word,
        idiom, proverb
    )
}