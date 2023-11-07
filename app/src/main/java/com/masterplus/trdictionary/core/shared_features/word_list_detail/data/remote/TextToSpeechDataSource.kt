package com.masterplus.trdictionary.core.shared_features.word_list_detail.data.remote

import com.masterplus.trdictionary.core.util.Resource

interface TextToSpeechDataSource {
    suspend fun synthesize(text: String): Resource<ByteArray>
}