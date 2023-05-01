package com.masterplus.trdictionary.features.word_detail.domain.repo

interface TTSRepo {

    fun speak(text: String)

    fun dispose()

}