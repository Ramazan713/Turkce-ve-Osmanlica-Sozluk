package com.masterplus.trdictionary.core.domain.enums

enum class WordType(
    val id: Int,
) {

    Default(
        id = 1
    ),
    PureWord(
        id = 2
    ),
    Idiom(
        id = 3
    ),
    Proverb(
        id = 4
    ),
    Unknown(
        id = 5
    );


    companion object{
        fun fromId(id: Int): WordType{
            return when(id){
                Default.id -> Default
                PureWord.id -> PureWord
                Idiom.id -> Idiom
                Proverb.id -> Proverb
                else -> Unknown
            }
        }
    }
}