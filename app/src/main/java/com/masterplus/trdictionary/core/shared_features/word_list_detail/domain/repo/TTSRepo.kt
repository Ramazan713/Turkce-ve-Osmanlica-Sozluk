package com.masterplus.trdictionary.core.shared_features.word_list_detail.domain.repo

interface TTSRepo {

    fun speak(text: String)

    fun dispose()

}