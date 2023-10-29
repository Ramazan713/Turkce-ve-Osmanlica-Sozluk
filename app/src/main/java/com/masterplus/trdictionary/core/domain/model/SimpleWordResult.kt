package com.masterplus.trdictionary.core.domain.model

import com.masterplus.trdictionary.core.domain.enums.DictType

data class SimpleWordResult(
    val word: Word,
    val meanings: List<Meaning>
){
    val meaning: String get() = meanings.firstOrNull()?.meaning ?: ""

    val wordContent: String get() = word.word

    val wordId get() = word.id

    val dictType: DictType get() = word.dictType
}
