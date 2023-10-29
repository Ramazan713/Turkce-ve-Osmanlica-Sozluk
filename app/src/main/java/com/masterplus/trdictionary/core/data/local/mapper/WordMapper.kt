package com.masterplus.trdictionary.core.data.local.mapper

import com.masterplus.trdictionary.core.data.local.entities.WordEntity
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType
import com.masterplus.trdictionary.core.domain.model.Word

fun WordEntity.toWord(): Word{
    return Word(
        id = id,
        prefix = prefix,
        suffix = suffix,
        word = word,
        countWord = 0,
        dictType = DictType.fromDicId(dictTypeId),
        wordType = WordType.fromId(wordTypeId),
        showTTS = showTTS,
        randomOrder = randomOrder
    )
}