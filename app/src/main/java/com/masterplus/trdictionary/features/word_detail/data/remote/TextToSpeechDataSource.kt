package com.masterplus.trdictionary.features.word_detail.data.remote

import com.masterplus.trdictionary.core.domain.util.Resource

interface TextToSpeechDataSource {
    suspend fun synthesize(text: String): Resource<ByteArray>
}