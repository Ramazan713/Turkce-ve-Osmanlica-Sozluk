package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.Meaning
import com.masterplus.trdictionary.core.domain.model.SimpleWordResult
import com.masterplus.trdictionary.core.domain.model.Word


fun simpleWordResult(
    wordId: Int = 1,
    wordType: WordType = WordType.PureWord,
    word: Word = word(id = wordId, wordType = wordType),
    meanings: List<Meaning> = listOf(meaning(wordId = wordId), meaning(id = 2, wordId = wordId))
): SimpleWordResult{
    return SimpleWordResult(
        word, meanings
    )
}