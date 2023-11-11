package com.masterplus.trdictionary.features.home.domain.enums

enum class ShortInfoEnum(val saveKey: String) {
    Proverb("shortInfoProverbRandom"),
    Idiom("shortInfoIdiomRandom"),
    Word("shortInfoWordRandom");


    companion object {
        fun from(saveKey: String): ShortInfoEnum {
            return when(saveKey){
                Proverb.saveKey -> Proverb
                Idiom.saveKey -> Idiom
                Word.saveKey -> Word
                else -> Word
            }
        }
    }

}