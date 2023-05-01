package com.masterplus.trdictionary.core.domain.model

import androidx.compose.ui.text.buildAnnotatedString
import com.masterplus.trdictionary.core.domain.enums.WordType

data class Word(
    val id: Int,
    val prefix: String?,
    val word: String,
    val suffix: String?,
    val countWord: Int,
    val dictType: WordType,
    val showTTS: Boolean,
    val randomOrder: Int,
){
    val allWordContent get() = buildAnnotatedString {
        if(prefix!=null) append("$prefix ")
        append(word)
        if(suffix!=null) append(", -${suffix}")
    }.text
}
