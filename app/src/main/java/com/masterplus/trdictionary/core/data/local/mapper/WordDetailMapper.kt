package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.views.WordDetailView
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.Word
import com.masterplus.trdictionary.features.word_detail.domain.model.WordDetail

fun WordDetailView.toWordDetail(): WordDetail{
    return WordDetail(
        inAnyList = inAnyList,
        inFavorite = inFavorite,
        hasCompoundWords = hasCompoundWords,
        id = id,
        prefix = prefix,
        word = word,
        suffix = suffix,
        searchWord = searchWord,
        showInQuery = showInQuery,
        randomOrder = randomOrder,
        dictType = DictType.fromDicId(dictTypeId),
        wordType = WordType.fromId(wordTypeId),
        showTTS = showTTS,
    )
}

fun WordDetail.toWord(): Word{
    return Word(
        id = id,
        prefix = prefix,
        word = word,
        suffix = suffix,
        randomOrder = randomOrder,
        dictType = dictType,
        wordType = wordType,
        showTTS = showTTS,
        countWord = 0
    )
}