package com.masterplus.trdictionary.core.utils.sample_data

import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.model.WordDetail


fun wordDetail(
    inAnyList: Boolean = true,
    inFavorite: Boolean = true,
    hasCompoundWords: Boolean = true,
    id: Int = 1,
    prefix: String? = null,
    word: String = "word $id",
    suffix: String? = null,
    searchWord: String = word,
    dictType: DictType = DictType.TR,
    showTTS: Boolean = true,
    showInQuery: Int = 1,
    randomOrder: Int = 1,
    wordType: WordType = WordType.PureWord
): WordDetail {
    return WordDetail(
        inAnyList = inAnyList,
        inFavorite = inFavorite,
        hasCompoundWords = hasCompoundWords,
        id = id,
        prefix= prefix,
        word = word,
        suffix = suffix,
        dictType = dictType,
        showTTS = showTTS,
        randomOrder = randomOrder,
        wordType = wordType,
        searchWord = searchWord,
        showInQuery = showInQuery
    )
}