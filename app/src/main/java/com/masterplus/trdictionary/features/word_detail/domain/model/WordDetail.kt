package com.masterplus.trdictionary.features.word_detail.domain.model

import androidx.compose.ui.text.buildAnnotatedString
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.WordType

data class WordDetail(
    val inAnyList: Boolean,
    val inFavorite: Boolean,
    val hasCompoundWords: Boolean,
    val id: Int,
    val prefix: String?,
    val word: String,
    val suffix: String?,
    val searchWord: String,
    val showInQuery: Int,
    val randomOrder: Int,
    val dictType: DictType,
    val wordType: WordType,
    val showTTS: Boolean
){
    val hasProverbIdioms: Boolean get() {
        return arrayOf(
            WordType.Proverb,
            WordType.Idiom,
            WordType.PureWord
        ).contains(wordType)
    }

    val allWordContent get() = buildAnnotatedString {
        if(prefix!=null) append("$prefix ")
        append(word)
        if(suffix!=null) append(", -${suffix}")
    }.text
}