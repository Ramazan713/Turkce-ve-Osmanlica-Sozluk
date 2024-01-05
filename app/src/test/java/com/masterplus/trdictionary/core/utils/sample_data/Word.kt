package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.Word


fun word(
    id: Int = 1,
    prefix: String? = null,
    word: String = "word $id",
    suffix: String? = null,
    countWord: Int = 1,
    dictType: DictType = DictType.TR,
    showTTS: Boolean = true,
    randomOrder: Int = 1,
    wordType: WordType = WordType.PureWord
): Word{
    return Word(id, prefix, word, suffix, countWord, dictType, showTTS, randomOrder, wordType)
}